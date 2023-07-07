import java.util.*;

public class Model {
    // Класс клетки
    public class Cell {
        private CellType type; // Тип клетки
        private int x; // Координата x
        private int y; // Координата y
        private List<Cell> neighbors; // Список соседних клеток

        public Cell(CellType type, int x, int y) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.neighbors = new ArrayList<>();
        }

        public CellType getType() {
            return type;
        }

        public void setType(CellType type) {
            this.type = type;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public List<Cell> getNeighbors() {
            return neighbors;
        }

        public void addNeighbor(Cell neighbor) {
            neighbors.add(neighbor);
        }
    }

    // Перечисление типов клеток
    public enum CellType {
        START,
        END,
        BLOCKED,
        DEFAULT
    }

    public enum Heuruistics {
        EUCLIDIANDISTANCE,
        MANHATTANDISTANCE,
        DIAGONALDISTANCE
    }

    private Cell[][] grid; // Массив клеток
    private Cell startCell; // Стартовая клетка
    private Cell goalCell; // Конечная клетка
    private PriorityQueue<Cell> priorityQueue; // Очередь с приоритетом

    private Heuruistics heuristicType;

    public void setHeuristicType(Heuruistics heuristicType) {
        this.heuristicType = heuristicType;
    }

    // Конструктор класса Model
    public Model(int width, int height) {
        grid = new Cell[height][width];
        priorityQueue = new PriorityQueue<>();
        initializeGrid();
    }

    //Установка типа клетки по координатам
    public void setCellType(int x, int y, CellType type) {
        grid[x][y].setType(type);
    }

    // Инициализация сетки клеток
    private void initializeGrid() {
        // Создание клеток и заполнение сетки
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                // Создание клетки с типом NORMAL и координатами (j, i)
                Cell cell = new Cell(CellType.DEFAULT, j, i);
                grid[i][j] = cell;
            }
        }

        // Установка соседних клеток (включая диагональные соседи)
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Cell cell = grid[i][j];
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) {
                            continue; // Пропускаем текущую клетку
                        }
                        int newX = j + dx;
                        int newY = i + dy;
                        if (newX >= 0 && newX < grid[0].length && newY >= 0 && newY < grid.length) {
                            // Связываем клетку с соседней клеткой
                            Cell neighbor = grid[newY][newX];
                            cell.addNeighbor(neighbor);
                        }
                    }
                }
            }
        }
    }

    public List<Cell> runAStar(Heuruistics heuristicType) {
        // Проверка наличия стартовой и конечной клеток
        if (startCell == null || goalCell == null) {
            throw new IllegalStateException("Start cell or goal cell is not set.");
        }

        // Создание карты для хранения стоимости пути до каждой клетки
        int[][] costMap = new int[grid.length][grid[0].length];

        // Инициализация стоимости пути для всех клеток как бесконечность
        for (int i = 0; i < grid.length; i++) {
            Arrays.fill(costMap[i], Integer.MAX_VALUE);
        }

        // Создание карты для хранения предшествующей клетки для каждой клетки
        Cell[][] previousCellMap = new Cell[grid.length][grid[0].length];

        // Создание очереди с приоритетом для хранения клеток
        PriorityQueue<Cell> queue = new PriorityQueue<>((a, b) -> {
            int priorityA = costMap[a.getY()][a.getX()] + calculateHeuristic(a, heuristicType);
            int priorityB = costMap[b.getY()][b.getX()] + calculateHeuristic(b, heuristicType);
            return Integer.compare(priorityA, priorityB);
        });

        // Установка стартовой клетки и ее стоимости пути равной 0
        costMap[startCell.getY()][startCell.getX()] = 0;
        queue.offer(startCell);

        while (!queue.isEmpty()) {
            Cell currentCell = queue.poll();

            if (currentCell.equals(goalCell)) {
                return reconstructPath(previousCellMap, currentCell); // Возвращаем путь в виде массива клеток
            }

            List<Cell> neighbors = currentCell.getNeighbors();
            for (Cell neighbor : neighbors) {
                int newCost = costMap[currentCell.getY()][currentCell.getX()] + 1;

                if (newCost < costMap[neighbor.getY()][neighbor.getX()]) {
                    costMap[neighbor.getY()][neighbor.getX()] = newCost;
                    previousCellMap[neighbor.getY()][neighbor.getX()] = currentCell;

                    // Обновление приоритета соседней клетки и ее добавление в очередь с приоритетом
                    if (queue.contains(neighbor)) {
                        queue.remove(neighbor);
                    }
                    queue.offer(neighbor);
                }
            }
        }

        return null; // Возвращаем null, если путь не найден
    }

    // Восстановление пути из предшествующих клеток
    private List<Cell> reconstructPath(Cell[][] previousCellMap, Cell currentCell) {
        List<Cell> path = new ArrayList<>();
        while (currentCell != null) {
            path.add(currentCell);
            currentCell = previousCellMap[currentCell.getY()][currentCell.getX()];
        }
        Collections.reverse(path);
        return path;
    }

    // Вычисление эвристической оценки для клетки в зависимости от выбранного типа эвристики
    private int calculateHeuristic(Cell cell, Heuruistics heuristicType) {
        int dx = Math.abs(cell.getX() - goalCell.getX());
        int dy = Math.abs(cell.getY() - goalCell.getY());

        switch (heuristicType) {
            case EUCLIDIANDISTANCE:
                return (int) Math.sqrt(dx * dx + dy * dy);
            case MANHATTANDISTANCE:
                return dx + dy;
            case DIAGONALDISTANCE:
                return Math.max(dx, dy);
            default:
                throw new IllegalArgumentException("Invalid heuristic type.");
        }
    }
    
    public void clear() {
        // Очистка массива клеток
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = null;
            }
        }

        // Очистка очереди с приоритетом
        priorityQueue.clear();
    }
}
