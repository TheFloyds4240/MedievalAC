package ac.grim.grimac.utils.data;

public final class KnownInput {
    private final boolean forward;
    private final boolean backward;
    private final boolean left;
    private final boolean right;
    private final boolean jump;
    private final boolean shift;
    private final boolean sprint;

    public KnownInput(boolean forward, boolean backward, boolean left, boolean right,
                      boolean jump, boolean shift, boolean sprint) {
        this.forward = forward;
        this.backward = backward;
        this.left = left;
        this.right = right;
        this.jump = jump;
        this.shift = shift;
        this.sprint = sprint;
    }

    public boolean forward() { return forward; }
    public boolean backward() { return backward; }
    public boolean left() { return left; }
    public boolean right() { return right; }
    public boolean jump() { return jump; }
    public boolean shift() { return shift; }
    public boolean sprint() { return sprint; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnownInput that = (KnownInput) o;
        return forward == that.forward &&
                backward == that.backward &&
                left == that.left &&
                right == that.right &&
                jump == that.jump &&
                shift == that.shift &&
                sprint == that.sprint;
    }

    @Override
    public int hashCode() {
        int result = forward ? 1 : 0;
        result = 31 * result + (backward ? 1 : 0);
        result = 31 * result + (left ? 1 : 0);
        result = 31 * result + (right ? 1 : 0);
        result = 31 * result + (jump ? 1 : 0);
        result = 31 * result + (shift ? 1 : 0);
        result = 31 * result + (sprint ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "KnownInput[" +
                "forward=" + forward + ", " +
                "backward=" + backward + ", " +
                "left=" + left + ", " +
                "right=" + right + ", " +
                "jump=" + jump + ", " +
                "shift=" + shift + ", " +
                "sprint=" + sprint + ']';
    }
}
