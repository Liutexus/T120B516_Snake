/** @author Eimutis Karčiauskas, KTU IF Programų inžinerijos katedra, 2014 09 09
   *
   *  Tai yra sisteminė ekrano valdymo klasė, skirta programavimo studijoms. 
   *  Atkreipkite dėmesį į refresh panaudojimo būtinybę, atnaujinant vaizdą.
   *  IŠBANDYKITE įvairius metodus, modifikuokite juos.
   *  EKSPERIMENTUOKITE su spalvomis ir ekrano formavimo algoritmais.
   *  SURAŠYKITE reikiamus veiksmus neužbaigtiems metodams.
   ****************************************************************************/
package studijosKTU;
import java.awt.*;
import static java.awt.Color.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;

public class ScreenKTU extends Canvas 
        implements MouseListener, MouseMotionListener, KeyListener, Runnable {

    public void print(Object get, Object get0, Color black) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static enum Grid {OFF, ON};
    public static enum Fonts {plainA, boldA, plainB, boldB,  plainC, boldC};
    final private static String[] fontSet = {"Courier", "Monospaced", "Meiryo"};
    public static final char SMILE1 ='\u263a', SMILE2 ='\u263b';
    public static final char symArrows[] = {'\u2191', '\u2197',
           '\u2192', '\u2198', '\u2193', '\u2199', '\u2190', '\u2196'};
    public static final Color[] colors = 
            {white, yellow, orange, cyan, lightGray, magenta,
             pink, red, green, blue, gray, darkGray, black};
    final protected int cellH, cellW;  // celės matavimai kartu su grid'u 
    final private int cellH0, cellW0;  // celės matavimai nevertinant grid'o 
    final static private Dimension display = Toolkit.getDefaultToolkit().getScreenSize();
    private static int startX=0, startY=0, maxHeight = 0;
    final protected int screenH, screenW;
    final protected int grid;
    final protected int totalH, totalW;
    private Color backColor = Color.blue;
    private Color fontColor = Color.white;
    final private Color gridColor = Color.darkGray;
    final private JFrame mainFrame = new JFrame("\u00a9 KTU IF \u00a9");    
    final private Image buffImage;
    final protected Graphics g;
    
    public int xDirection = 0;
    public int yDirection = 0;
    
    public ScreenKTU(){
        this(18, 12, 20, 40);
    }
    public ScreenKTU(Grid gr){
        this(18, 12, 20, 40, gr);
    }
    public ScreenKTU(int cellSize, int screenSize){
        this(cellSize, cellSize, screenSize, screenSize);
    }
    public ScreenKTU(int cellSize, int screenSize, Grid gr){
        this(cellSize, cellSize, screenSize, screenSize, gr);
    }
    public ScreenKTU(int cellSize, int screenSize, Fonts sf){
        this(cellSize, cellSize, screenSize, screenSize, sf);
    }
    public ScreenKTU(int ch, int cw, int sh, int sw){
        this(ch, cw, sh, sw, Fonts.plainA);
    }
    public ScreenKTU(int ch, int cw, int sh, int sw, Fonts sf){
        this(ch, cw, sh, sw, sf, Grid.OFF);
    }
    public ScreenKTU(int ch, int cw, int sh, int sw, Grid gr){
        this(ch, cw, sh, sw, Fonts.plainA, gr);
    }
    public ScreenKTU(int ch, int cw, int sh, int sw, Fonts sf, Grid gr){
        super();
        grid = gr==Grid.OFF? 0: 1;
        cellH0 = ch; cellW0 = cw;
        cellH = cellH0 + grid; cellW = cellW0 + grid; 
        screenH = sh; screenW = sw;
        totalH = cellH * screenH; 
        totalW = cellW * screenW;
        createFrame();
        Image im = null;
        do {
            im = createImage(totalW, totalH);
            try {Thread.sleep(10);} catch (InterruptedException e) {  };
        } while(im == null);
        buffImage = im;
        g = buffImage.getGraphics(); 
        int fontOrd = sf.ordinal();
        g.setFont(new Font(fontSet[fontOrd/2], fontOrd&1, cellH0-3)); 
        fm = g.getFontMetrics(g.getFont());
        yOffset = fm.getAscent()-fm.getDescent()/2-fm.getLeading()+
                    (fontOrd<=3? 0: 3);  
        g.setColor(gridColor);
        g.fillRect(0,0, totalW, totalH); 
        fillRect(0, 0, screenH, screenW);
        refresh();
    }
    private void createFrame(){
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().add(this);
        mainFrame.pack();
        int frameH = (int)(mainFrame.getBounds().getHeight());
        int frameW = (int)(mainFrame.getBounds().getWidth());
        if ((startX + frameW) > display.width+10) {
            startX = 0;
            if((startY += maxHeight) > display.height-40)
                startY = 40;
            maxHeight = 0;
        }
        mainFrame.setLocation(startX, startY);
        if(frameH > maxHeight) maxHeight = frameH;
        startX += frameW;
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        mainFrame.setVisible(true);   
    }
    @Override
  public void paint(Graphics gr) {
      gr.drawImage(buffImage, 0, 0, this);
  }
    @Override
  public void update(Graphics gr) {
      gr.drawImage(buffImage, 0, 0, this);
  }
    @SuppressWarnings("empty-statement")
    public void refresh(long t){
        repaint();
        try {Thread.sleep(t);} catch (Exception e) { };
    }
    final public void refresh(){
        refresh(100);
        //refresh(1);
    }
    public void setColors(Color bc, Color fc){
        backColor = bc;
        fontColor = fc;
    }
    public void setBackColor(Color bc){
        backColor = bc;
    }
    public void setFontColor(Color fc){
        fontColor = fc;
    }
    public void setTitle(String title){
        mainFrame.setTitle(title);
    }
    public void setLocation(int x, int y){
        mainFrame.setLocation(x, y);
        mainFrame.setVisible(true);
    }
// šis metodas naudoja Lambda notaciją    
    public void forEachCell(Consumer2D action){
        for(int row=0; row<screenH; row++)
            for(int col=0; col<screenW; col++)
                action.accept(row, col);
            
    }
    final private FontMetrics fm;
    final private int yOffset; 
    final static private char[] drawChars = new char[2];
    
    final synchronized public void print(int row, int col, char ch){
        int xOffset = (cellW0 - fm.charWidth(ch))/2;
        int x = col * cellW;
        int y = row * cellH;
        if(backColor != null){
            g.setColor(backColor);
            g.fillRect(x, y, cellW0, cellH0);
        }
        if(cellH0>=10){
            drawChars[0] = ch;
            g.setColor(fontColor);
            g.drawChars(drawChars, 0, 1, x+xOffset, y+yOffset);
        }
    }
    final public void print(int row, int col, String s){
        for(int i=0; i<s.length(); i++)
            print(row, col+i, s.charAt(i));
    }
    final public void print(int row, int col, Color c){
        int x = col * cellW;
        int y = row * cellH;
        g.setColor(c);
        g.fillRect(x, y, cellW0, cellH0);        
    }
    final public void print(int row, int col, Object o){
        print(row, col, o.toString());        
    }
    final public void fillRect(int rowTop, int columnLeft, 
                         int countRow, int countColumn){
        if(grid==0){
            int x = columnLeft * cellW;
            int y = rowTop * cellH;
            g.setColor(backColor);
            g.fillRect(x, y, countColumn*cellW, countRow*cellH);
        } else {
            for(int row=rowTop; row<rowTop+countRow; row++)
              for(int col=columnLeft; col<columnLeft+countColumn; col++)
                  print(row, col, backColor);    
        } 
    }
    final public void fillRect(int rowTop, int columnLeft, 
                         int countRow, int countColumn, char ch){
        for(int row=rowTop; row<rowTop+countRow; row++)
          for(int col=columnLeft; col<columnLeft+countColumn; col++)
              print(row, col, ch);
    }
    final public void fillRect(int rowTop, int columnLeft, 
                         int countRow, int countColumn, Color bc){
        if(grid==0){
            int x = columnLeft * cellW;
            int y = rowTop * cellH;
            g.setColor(bc);
            g.fillRect(x, y, countColumn*cellW, countRow*cellH);
        } else {
            for(int row=rowTop; row<rowTop+countRow; row++)
              for(int col=columnLeft; col<columnLeft+countColumn; col++)
                  print(row, col, bc);    
        }
    }
    final public void fillBorder(int rowTop, int columnLeft, 
                         int countRow, int countColumn, char ch){
        for(int row=rowTop; row<rowTop+countRow; row++){
            print(row, columnLeft, ch);
            print(row, columnLeft+countColumn-1, ch);
        }
        for(int col=columnLeft; col<columnLeft+countColumn; col++){
            print(rowTop, col, ch);
            print(rowTop+countRow-1, col, ch);
        }
    }
    final public void fillBorder(int rowTop, int columnLeft, 
                         int countRow, int countColumn){
        for(int row=rowTop; row<rowTop+countRow; row++){
            print(row, columnLeft, backColor);
            print(row, columnLeft+countColumn-1, backColor);
        }
        for(int col=columnLeft; col<columnLeft+countColumn; col++){
            print(rowTop, col, backColor);
            print(rowTop+countRow-1, col, backColor);
        }
    }
    final public void fillBorder(int rowTop, int columnLeft, 
                         int countRow, int countColumn, Color bc){
        for(int row=rowTop; row<rowTop+countRow; row++){
            print(row, columnLeft, bc);
            print(row, columnLeft+countColumn-1, bc);
        }
        for(int col=columnLeft; col<columnLeft+countColumn; col++){
            print(rowTop, col, bc);
            print(rowTop+countRow-1, col, bc);
        }
    }
    final public void lineBorder(int rowTop, int columnLeft, 
                           int countRow, int countColumn, int lineType){
        int x = columnLeft * cellW+ cellW/2;
        int y = rowTop * cellH + cellH/2;
        g.setColor(fontColor);
        int dx = (countColumn-1)*cellW;
        int dy = (countRow-1)*cellH;
        switch(lineType % 4){
            case 3: g.drawRect(x-1, y-1, dx+2, dy+2);        
            case 2: g.drawRect(x-2, y-2, dx+4, dy+4);
            case 1: g.drawRect(x, y, dx, dy);        
        }
    }  
    final public void drawLine(int rowTop, int columnLeft, 
                           int countRow, int countColumn, int lineType){
        if(countRow != 0) countRow--;
        if(countColumn != 0) countColumn--;
        int x1 = columnLeft * cellW + cellW/2;
        int y1 = rowTop * cellH + cellH/2;
        int x2 = x1 + countColumn*cellW;
        int y2 = y1 + countRow*cellH;
        g.setColor(fontColor);
        switch(lineType % 4){
            case 3: g.drawLine(x1-1, y1-1, x2-1, y2-1);        
            case 2: g.drawLine(x1-2, y1-2, x2-2, y2-2);
            case 1: g.drawLine(x1, y1, x2, y2);        
        }
    }  
    final public void clearAll(Color color){
        setBackColor(color);
        fillRect(0, 0, screenH, screenW);
    }
    final public void printRowsColumnsNumbers(){
        print(0, 0, '0');
        for(int i=1; i<screenH; i++){
            print(i, 0, (char)('0' + i%10));
            if(i%10 == 0) 
                print(i, 1, (char)('0' + i/10));
        }
        for(int j=1; j<screenW; j++){
            print(0, j, (char)('0' + j%10));
            if(j%10 == 0) 
                print(1, j, (char)('0' + j/10));
        }
    }
    @Override
    public Dimension getPreferredSize() {
      return new Dimension(totalW, totalH);
    }
    @Override
    public Dimension getMinimumSize() {
      return getPreferredSize();
    }
    public void mouseClicked(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void mousePressed(MouseEvent e) {
    //        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
    //        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
    //        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseReleased(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void run() {
        throw new UnsupportedOperationException("Reikia realizuoti run metodą");
    }

//    void exploreFontsProperties(){
//        for(int k=8; k<=60; k++){
//             FontMetrics fm = getFontMetrics(new Font("Serif", Font.PLAIN, k));
//             int a = fm.getAscent();
//             int b = fm.getDescent();
//             int c = fm.getLeading();
//             System.out.printf("%2d %2d %2d %2d\n", k, a, b, c);
//        }
//    }
//    char[] chrs = "MINTIS╳╳↖↗↘↙ąęgqČŠ\u263a\u263b*+@\u2600\u2602".toCharArray();
//    void drawBoard(){
//        setFontColor(Color.red);
//        int k=0;
//        for(int i = 0; i < screenH; i++) 
//            for(int j = 0; j < screenW; j++){ 
//                setBackColor((i+j)%2==0?Color.green:Color.gray);
//                print(i,j, chrs[k++]);
////                Toolkit.getDefaultToolkit().beep(); 
//            }
//            refresh(10);
//    }
//    void demoFonts(){
//        drawBoard();
////        System.out.println(new String(chrs));
//    }
//    public static void main(String... ps) {
//        for(int fnt=0; fnt<4; fnt++)
//        for(int cs=20; cs<36; cs+=2){
//            ScreenKTU sc = new ScreenKTU(cs, cs, 4, 6, fnt);
//            sc.demoFonts();
//        }
//    }  

    public void keyTyped(KeyEvent ke) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void keyPressed(KeyEvent ke) {
        // System.out.println(ke.getKeyCode() + " Pressed");
        boolean acceptInput = true;
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                if(xDirection == 0 && acceptInput == true)
                {
                    xDirection = 1;
                    yDirection = 0;
                    acceptInput = false;
                }   break;
            case KeyEvent.VK_LEFT:
                if(xDirection == 0 && acceptInput == true)
                {
                    xDirection = -1;
                    yDirection = 0;
                    acceptInput = false;
                }
                break;
            case KeyEvent.VK_UP:
                if(yDirection == 0 && acceptInput == true)
                {
                    yDirection = -1;
                    xDirection = 0;
                    acceptInput = false;
                }
                break;
            case KeyEvent.VK_DOWN:
                if(yDirection == 0 && acceptInput == true)
                {
                    yDirection = 1;
                    xDirection = 0;
                    acceptInput = false;
                }
                break;
            default:
                break;
        }
        
        if(ke.getKeyCode() == KeyEvent.VK_SPACE)
        {
            yDirection = 0;
            xDirection = 0;
        }
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void keyReleased(KeyEvent ke) {
        //System.out.println("Released");
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseDragged(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseMoved(MouseEvent me) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
