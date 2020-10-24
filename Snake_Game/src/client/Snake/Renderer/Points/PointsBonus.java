package client.Snake.Renderer.Points;

public class PointsBonus implements PointsStrategy{

    @Override
    public int Calculate(int a, int b) {
        return (a + b) * 2;
    }
}