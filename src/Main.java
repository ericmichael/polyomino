
public class Main {

    String[] emptyGlues = new String[4];
    public PolyTile tetrisF(){
        String[] gleft = {null, null, null, "b"};
        String[] gright = {null, "d" ,null, null};
        PolyTile tetrisF = new PolyTile();
        tetrisF.addTile(0,0, emptyGlues);
        tetrisF.addTile(0,-1,emptyGlues);
        tetrisF.addTile(-1,0,emptyGlues);
        tetrisF.addTile(0,1,gleft);
        tetrisF.addTile(1,1,gright);
        return tetrisF;
    }

    public PolyTile tetrisI(){
        String[] gtop = {null,"a",null, null};
        String[] gbottom = {null, "b", null, null};
        PolyTile tetris = new PolyTile();
        tetris.addTile(0,0, emptyGlues);
        tetris.addTile(0,1,emptyGlues);
        tetris.addTile(0,-1,emptyGlues);
        tetris.addTile(0,2,gtop);
        tetris.addTile(0,-2,gbottom);
        return tetris;
    }

    public PolyTile tetrisL(){
        String[] gtop = {null, "c",null, "a"};
        PolyTile tetris = new PolyTile();
        tetris.addTile(0,0, emptyGlues);
        tetris.addTile(0,1,emptyGlues);
        tetris.addTile(0,2,emptyGlues);
        tetris.addTile(0,3,gtop);
        tetris.addTile(1,0,emptyGlues);
        return tetris;
    }



    public static void main(String args[]) {

    }
}