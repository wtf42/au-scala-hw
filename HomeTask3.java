interface Base {
    public int getX();

    public void setX(int x);

    public int getY();

    public int foo();

    public int goo();
}

abstract class BaseClass {
    public static void init(Base that) {
        that.setX(1);
    }

    public static int getY(Base that) {
        return that.goo();
    }

    public static int goo(Base that) {
        return that.getX() + 2;
    }
}

final class InhObject implements Runnable {
    public static final InhObject object = new InhObject();

    private InhObject() {
    }

    @Override
    public void run() {
    }

    private int foo() {
        return 4;
    }
}

class Inh implements Base {
    private int x;
    private int y;
    private volatile boolean yComputed;

    public Inh() {
        BaseClass.init(this);
    }

    public static void run() {
        InhObject.object.run();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return yComputed ? y : yLazyCompute();
    }

    @Override
    public int goo() {
        return BaseClass.goo(this);
    }

    @Override
    public int foo() {
        return 3;
    }

    private synchronized int yLazyCompute() {
        if (!yComputed) {
            y = BaseClass.getY(this);
            yComputed = true;
        }
        return y;
    }
}
