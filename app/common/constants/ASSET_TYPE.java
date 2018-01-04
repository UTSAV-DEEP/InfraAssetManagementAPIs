package common.constants;

public enum ASSET_TYPE {
    LAPTOP(900),KEYBOARD(2),MOUSE(50),MONITOR(15),DONGLE(12);
    private int price;
    ASSET_TYPE(int p) {
        price = p;
    }
    int getPrice() {
        return price;
    }
}
