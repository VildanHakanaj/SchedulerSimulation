import java.util.Objects;

public class Table {

    private final String DEFAULT_ALIGNMENT = "center";

    private String title;
    private String[][] data;
    private char[][] charArray;
    private int[][] cellAlignment;
    private int titleAlignment;
    private int numRows, numCols;
    private int cellInnerHeight, cellInnerWidth;
    private int cellOuterHeight, cellOuterWidth;
    private int tableHeight, tableWidth;

    // Constructor.
    // All rows passed within data must have the same number of elements (cannot be a jagged 2D array).
    public Table (String title, String[][] data) {
        this.title = title;
        this.data = memberwiseCloneData(data);
        this.numRows = this.data.length;
        this.numCols = this.data[0].length;
        this.cellInnerHeight = 1;
        this.cellInnerWidth = findInnerCellWidth();
        this.cellOuterHeight = this.cellInnerHeight + 2;
        this.cellOuterWidth = this.cellInnerWidth + 2;
        this.tableHeight = (numRows * cellInnerHeight) + numRows + 1;
        this.tableWidth = (numCols * cellInnerWidth) + numCols + 1;
        charArray = new char[tableHeight][tableWidth];
        cellAlignment = new int[numRows][numCols];
        setTitleAlignment(this.DEFAULT_ALIGNMENT);
        setAllCellsToDefaultAlignment();
    }

    public void draw() {
        drawTitleBox();
        // Populate the character array with borders and data.
        populateArrayWithBorders();
        populateArrayWithData();
        // Draw the character array to the console.
        for (int i = 0; i < this.tableHeight; i++) {
            for (int j = 0; j < this.tableWidth; j++) {
                System.out.print(this.charArray[i][j]);
            }
            System.out.println();
        }
    }

    public void setTitleAlignment(String alignment) {
        // Does nothing if alignment does not equal 'left', 'right', or 'center'.
        if(Objects.equals(alignment.toUpperCase(), "LEFT")) {
            this.titleAlignment = -1;
        } else if(Objects.equals(alignment.toUpperCase(), "CENTER")) {
            this.titleAlignment = 0;
        } else if(Objects.equals(alignment.toUpperCase(), "RIGHT")) {
            this.titleAlignment = 1;
        }
    }

    public void setCellAlignment(int row, int col, String alignment) {
        // Does nothing if alignment does not equal 'left', 'right', or 'center'.
        if(Objects.equals(alignment.toUpperCase(), "LEFT")) {
            this.cellAlignment[row][col] = -1;
        } else if(Objects.equals(alignment.toUpperCase(), "CENTER")) {
            this.cellAlignment[row][col] = 0;
        } else if(Objects.equals(alignment.toUpperCase(), "RIGHT")) {
            this.cellAlignment[row][col] = 1;
        }
    }

    public void setRowAlignment(int row, String alignment) {
        for (int i = 0; i < this.numCols; i++) {
            setCellAlignment(row, i, alignment);
        }
    }

    public void setColAlignment(int col, String alignment) {
        for (int i = 0; i < this.numCols; i++) {
            setCellAlignment(i, col, alignment);
        }
    }

    public void setAllCellsToDefaultAlignment() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                setCellAlignment(i, j, this.DEFAULT_ALIGNMENT);
            }
        }
    }

    private boolean titleIsLeftAligned() {
        if(this.titleAlignment == -1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean titleIsCenterAligned() {
        if(this.titleAlignment == 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean titleIsRightAligned() {
        if(this.titleAlignment == 1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean cellIsLeftAligned(int row, int col) {
        if(this.cellAlignment[row][col] == -1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean cellIsCenterAligned(int row, int col) {
        if(this.cellAlignment[row][col] == 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean cellIsRightAligned(int row, int col) {
        if(this.cellAlignment[row][col] == 1) {
            return true;
        } else {
            return false;
        }
    }

    private void drawTitleBox() {
        for (int i = 0; i < this.tableWidth; i++) {
            if(i == 0 || i == (this.tableWidth - 1)) {
                System.out.print('+');
            } else {
                System.out.print('-');
            }
        }
        System.out.println("\n|" + getAlignedTitleString() + "|");
    }

    private void populateArrayWithData() {
        int xCharIndex, yCharIndex;
        String currentString;
        // Loop through the rows and columns of data (strings) to be displayed in the table.
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // Get the data (String) at the currently indexed row/col in its proper alignment.
                currentString = getAlignedDataString(i,j);
                // Calculate the location to which the currently indexed data will be placed within the table.
                yCharIndex = (i * (cellOuterHeight - 1)) + 1;
                xCharIndex = (j * (cellOuterWidth - 1)) + 1;
                // Loop through the current string, copying over characters to the charArray at the correct positions.
                for (int k = 0; k < currentString.length(); k++) {
                    charArray[yCharIndex][xCharIndex + k] = currentString.charAt(k);
                }
            }
        }
    }

    private void populateArrayWithBorders() {
        for (int i = 0; i < this.tableHeight; i++) {
            for (int j = 0; j < this.tableWidth; j++) {
                if(i % (this.cellInnerHeight + 1) == 0 && j % (this.cellInnerWidth + 1) == 0) {
                    this.charArray[i][j] = '+';
                } else if (i % (this.cellInnerHeight + 1) == 0) {
                    this.charArray[i][j] = '-';
                } else if (j % (this.cellInnerWidth + 1) == 0) {
                    this.charArray[i][j] = '|';
                }
            }
        }
    }

    private int findInnerCellWidth() {
        int n = 0;
        if (this.data != null) {
            for (String[] strArray : this.data) {
                for (String str : strArray) {
                    if (str.length() > n) {
                        n = str.length();
                    }
                }
            }
        }
        return n;
    }

    private String[][] memberwiseCloneData(String[][] data) {
        String[][] clone = new String[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                clone[i][j] = data[i][j];
            }
        }
        return clone;
    }

    private String getAlignedTitleString() {
        String currentString, ellipses, temp;
        int padding, titleBoxWidth, amountOfCharsToCopy;
        currentString = this.title;
        ellipses = " ...";
        temp = "";
        titleBoxWidth = this.tableWidth - 2;
        // Shorten title String and append ellipses to the end in the case that the title is too large for the allotted title-box space.
        if(currentString.length() > titleBoxWidth) {
            amountOfCharsToCopy = titleBoxWidth - ellipses.length();
            for (int i = 0; i < amountOfCharsToCopy; i++) {
                temp = temp + currentString.charAt(0);
            }
            currentString = temp + ellipses;
        }
        // Set padding on the current string depending on its cell alignment.
        if(currentString.length() < titleBoxWidth) {
            padding = titleBoxWidth - currentString.length();
            if (titleIsLeftAligned()) {
                currentString = padRight(currentString, ' ', padding);
            }
            if (titleIsRightAligned()) {
                currentString = padLeft(currentString, ' ', padding);
            }
            if (titleIsCenterAligned()) {
                currentString = padLeftRightEvenly(currentString, ' ', padding);
            }
        }
        return currentString;
    }

    private String getAlignedDataString(int row, int col) {
        String currentString;
        int padding;
        // Get the currently indexed string.
        currentString = this.data[row][col];
        // Set padding on the current string depending on its cell alignment.
        if(currentString.length() < this.cellInnerWidth) {
            padding = this.cellInnerWidth - currentString.length();
            if (cellIsLeftAligned(row, col)) {
                currentString = padRight(currentString, ' ', padding);
            }
            if (cellIsRightAligned(row, col)) {
                currentString = padLeft(currentString, ' ', padding);
            }
            if (cellIsCenterAligned(row, col)) {
                currentString = padLeftRightEvenly(currentString, ' ', padding);
            }
        }
        return currentString;
    }

    private String padLeft(String s, char c, int n) {
        for (int i = 0; i < n; i++) {
            s = c + s;
        }
        return s;
    }

    private String padRight(String s, char c, int n) {
        for (int i = 0; i < n; i++) {
            s = s + c;
        }
        return s;
    }

    private String padLeftRightEvenly(String s, char c, int n) {
        boolean padRight = true;
        for (int i = 0; i < n; i++) {
            if(padRight) {
                s = s + c;
            } else {
                s = c + s;
            }
            padRight = !padRight;   // Toggle boolean.
        }
        return s;
    }
}
