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

    public Cell runAStarStep(Heuruistics heuristicType) {
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

        // Создание очереди с приоритетом для хранения клеток
        PriorityQueue<Cell> queue = new PriorityQueue<>((a, b) -> Integer.compare(costMap[a.getY()][a.getX()], costMap[b.getY()][b.getX()]));

        // Установка стартовой клетки и ее стоимости пути равной 0
        costMap[startCell.getY()][startCell.getX()] = 0;
        queue.offer(startCell);

        while (!queue.isEmpty()) {
            // Извлечение клетки с наименьшей стоимостью пути из очереди
            Cell currentCell = queue.poll();

            // Проверка, достигли ли мы конечной клетки
            if (currentCell.equals(goalCell)) {
                return currentCell; // Возвращаем найденную конечную клетку
            }

            // Обработка соседних клеток
            List<Cell> neighbors = currentCell.getNeighbors();
            for (Cell neighbor : neighbors) {
                // Проверка, является ли клетка проходимой
                if (neighbor.getType() != CellType.BLOCKED) {
                    // Вычисление новой стоимости пути до соседней клетки
                    int newCost = costMap[currentCell.getY()][currentCell.getX()] + 1;

                    // Если новая стоимость пути меньше текущей стоимости пути до соседней клетки, обновляем стоимость и добавляем ее в очередь
                    if (newCost < costMap[neighbor.getY()][neighbor.getX()]) {
                        costMap[neighbor.getY()][neighbor.getX()] = newCost;

                        // Вычисление эвристической оценки для соседней клетки
                        int heuristic = calculateHeuristic(neighbor, heuristicType);

                        // Обновление предыдущей клетки для соседней клетки (в данном случае не используется)

                        // Добавление соседней клетки в очередь с приоритетом
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return null; // Возвращаем null, если путь не найден
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
                return 0;
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
