import java.util.Objects;

/*
    Class name: Table
    Purpose:    Creates a visual ascii table representing given input data in a visually appealing, compact and dynamic fashion.
*/
public class Table {

    //region Class Variables
    private final String DEFAULT_ALIGNMENT = "center";

    private String title;                           // The title to display at the top of the table.
    private String[][] data;                        // The data that will fill the cells of the table.
    private char[][] charArray;                     // The characters to print to the console.
    private int[][] cellAlignment;                  // The alignment of each individual cell in the table. (left=-1,center=0,right=1)
    private int titleAlignment;                     // The alignment of the tables' title. (left=-1,center=0,right=1)
    private int numRows, numCols;                   // The number of rows and the number of Columns in the table.
    private int cellInnerHeight, cellInnerWidth;    // The width and height of a cell within the 'border'.
    private int cellOuterHeight, cellOuterWidth;    // The width and height of a cell including the 'border'.
    private int tableHeight, tableWidth;            // The width and height of the table including the 'border'.
    //endregion

    /*
    Table() Constructor.
    Parameters: title   - The title that will be displayed in the table.
                data    - The 2D array of strings to populate the table.
                        - All rows passed within data must have the same number of elements (cannot be a jagged 2D array).
    */
    public Table (String title, String[][] data) {
        this.title = title;                                         // Set the title.
        this.data = memberwiseCloneData(data);                      // Set the table data to the value of the data given.
        this.numRows = this.data.length;                            // Set the the number of rows(table) to the number of rows in data.
        this.numCols = this.data[0].length;                         // Set the the number of cols(table) to the number of cols in data.
        this.cellInnerHeight = 1;                                   // Set the the inner cell height to 1 (Text will always be 1 char high).
        this.cellInnerWidth = findInnerCellWidth();                 // Calculate and set the inner cell width.
        this.cellOuterHeight = this.cellInnerHeight + 2;            // Calculate and set the outer cell height.
        this.cellOuterWidth = this.cellInnerWidth + 2;              // Calculate and set the outer cell width.
        this.tableHeight = (numRows * cellInnerHeight) + numRows + 1;// Calculate and store the table height.
        this.tableWidth = (numCols * cellInnerWidth) + numCols + 1; // Calculate and store the table width.
        charArray = new char[tableHeight][tableWidth];              // Init the array of characters to print.
        cellAlignment = new int[numRows][numCols];                  // Init the cell alignment array.
        setTitleAlignment(this.DEFAULT_ALIGNMENT);                  // Set the title to the default alignment.
        setAllCellsToDefaultAlignment();                            // Set all cells to default alignment.
    }

    /*
    Method Name:    draw
    Purpose:        Draws the table object to the console.
    */
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
            System.out.println();   // New line.
        }
    }

    /*
    Method Name:    setTitleAlignment
    Parameter:      alignment   - The alignment to set the title
    Purpose:        Sets the title's alignment.
    */
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

    /*
    Method Name:    setCelleAlignment
    Parameters:     row         - The cell's row
                    col         - The cell's column
                    alignment   - The alignment to set the cell
    Purpose:        Sets the cell's alignment.
    */
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

    /*
    Method Name:    setRowAlignment
    Parameters:     row         - The row which all cells within will have their alignment set.
                    alignment   - The alignment to set the cells.
    Purpose:        Sets an entire row's alignment.
    */
    public void setRowAlignment(int row, String alignment) {
        for (int i = 0; i < this.numCols; i++) {
            setCellAlignment(row, i, alignment);
        }
    }

    /*
    Method Name:    setColAlignment
    Parameters:     col         - The column which all cells within will have their alignment set.
                    alignment   - The alignment to set the cells.
    Purpose:        Sets an entire column's alignment.
    */
    public void setColAlignment(int col, String alignment) {
        for (int i = 0; i < this.numCols; i++) {
            setCellAlignment(i, col, alignment);
        }
    }

    /*
    Method Name:    setAllCellsToDefaultAlignment
    Purpose:        Sets all cells within the table to default alignment.
    */
    public void setAllCellsToDefaultAlignment() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                setCellAlignment(i, j, this.DEFAULT_ALIGNMENT);
            }
        }
    }

    /*
    Method Name:    titleIsLeftAligned
    Returns:        true if title is left aligned. false otherwise.
    */
    private boolean titleIsLeftAligned() {return (this.titleAlignment == -1)? true : false;}

    /*
    Method Name:    titleIsCenterAligned
    Returns:        true if title is center aligned. false otherwise.
    */
    private boolean titleIsCenterAligned() {return (this.titleAlignment == 0)? true : false;}

    /*
    Method Name:    titleIsRightAligned
    Parameters:     row - The cell's row
                    col - The cell's column
    Returns:        true if title is right aligned. false otherwise.
    */
    private boolean titleIsRightAligned() {return (this.titleAlignment == 1)? true : false;}

    /*
    Method Name:    cellIsLeftAligned
    Parameters:     row - The cell's row
                    col - The cell's column
    Returns:        true if cell is left aligned. false otherwise.
    */
    private boolean cellIsLeftAligned(int row, int col) {return (this.cellAlignment[row][col] == -1)? true : false;}

    /*
    Method Name:    cellIsCenterAligned
    Parameters:     row - The cell's row
                    col - The cell's column
    Returns:        true if cell is center aligned. false otherwise.
    */
    private boolean cellIsCenterAligned(int row, int col) {return (this.cellAlignment[row][col] == 0)? true : false;}

    /*
    Method Name:    cellIsRightAligned
    Parameters:     row - The cell's row
                    col - The cell's column
    Returns:        true if cell is right aligned. false otherwise.
    */
    private boolean cellIsRightAligned(int row, int col) {return (this.cellAlignment[row][col] == 1)? true : false;}

    /*
    Method Name:    drawTitleBox
    Purpose:        Draws the title box and the properly aligned title to the console.
    */
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

    /*
    Method Name:    populateArrayWithData
    Purpose:        Adds the properly aligned strings of data to their correct positions in the character array.
    */
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

    /*
    Method Name:    populateArrayWithBorders
    Purpose:        Adds borders to their correct positions in the character array.
    */
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

    /*
    Method Name:    findInnerCellWidth
    Purpose:        Calculates the inner cell width for all cells based on the largest string in the given data set.
    */
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

    /*
    Method Name:    memberWiseCloneData
    Parameters:     data    - The data to create a deep copy of.
    Returns:        A deep copy of the given data set of type String[][].
    */
    private String[][] memberwiseCloneData(String[][] data) {
        String[][] clone = new String[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                clone[i][j] = data[i][j];
            }
        }
        return clone;
    }

    /*
    Method Name:    getAlignedTitleString
    Returns:        Properly aligned title of type String.
    */
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

    /*
    Method Name:    getAlignedDataString
    Parameters:     row - The data's row
                    col - The data's column
    Returns:        Properly aligned data of type String.
    */
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

    /*
    Method Name:    padLeft
    Purpose:        To pad a given String by a given character on the left hand side a given amount of times.
    Parameters:     s   - The string to be padded
                    c   - The character that the String will be padded with
                    n   - The number of times that 's' will be padded with 'c'
    Returns:        Padded String.
    */
    private String padLeft(String s, char c, int n) {
        for (int i = 0; i < n; i++) {
            s = c + s;
        }
        return s;
    }

    /*
    Method Name:    padRight
    Purpose:        To pad a given String by a given character on the right hand side a given amount of times.
    Parameters:     s   - The string to be padded
                    c   - The character that the String will be padded with
                    n   - The number of times that 's' will be padded with 'c'
    Returns:        Padded String.
    */
    private String padRight(String s, char c, int n) {
        for (int i = 0; i < n; i++) {
            s = s + c;
        }
        return s;
    }

    /*
    Method Name:    padLeftRightEvenly
    Purpose:        To evenly pad a given String by a given character on both sides a given amount of times.
    Parameters:     s   - The string to be padded
                    c   - The character that the String will be padded with
                    n   - The number of times that 's' will be padded with 'c'
    Returns:        Padded String.
    */
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
