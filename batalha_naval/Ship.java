package batalha_naval;

class Ship {
    protected char type;
    protected int size;
    protected boolean horizontal;
    protected String name;

    public Ship(char type, int size, String name) {
        this.type = type;
        this.size = size;
        this.name = name;
    }

    public char getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public String getName() {
        return name;
    }
}