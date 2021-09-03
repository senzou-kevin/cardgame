package gamelogic;

import akka.actor.ActorRef;
import annotation.IOCResource;
import annotation.IOCService;
import commands.BasicCommands;

import resourcesManage.ObjectIOC;
import structures.GameState;
import structures.basic.*;

import java.util.*;
import java.util.stream.Collectors;


@IOCService(name = "UnitMove")
public class UnitMove {
    @IOCResource
    private UnitMoveSender unitMoveSender;
    @IOCResource
    private AttackContainer attackContainer;
    @IOCResource
    private Attack attack;
    private int bestSize=10;
    private volatile boolean isMoving=false;
    private final int STEP=1;
    private GameState currentState;
    private ArrayList<Tile> openlist=new ArrayList<>();
    private ArrayList<Tile> closelist=new ArrayList<>();
    public void bindGameState(GameState state)
    {
        this.currentState=state;
    }
    private ArrayList<Tile>totalTiles=new ArrayList<>();
    private ArrayList<Tile>attackTiles=new ArrayList<>();
    private ArrayList<Tile>path=new ArrayList<>();

    public GameState getCurrentState() {
        return currentState;
    }
    // Commands
    /** unit move*/
    public void UnitMoveAndAttackCom(ActorRef out,Tile starttile, Tile endtile)
    {
        if(currentState.getCurrentUnit().getAttackScopeMode()==1&&endtile.getUnit()!=null)
        {
            attack.startAttack(out,currentState,currentState.getCurrentUnit(),endtile);
            MoveEnd();
            return;
            //currentState.setCanAttack(true);
        }
        else if(!totalTiles.contains(endtile)&&attackTiles.contains(endtile))
        {

            path=FindBestPath(starttile,endtile);
            if(path.size()>1)
            {
                System.out.println("攻击》11");
                AttackMessage message=new AttackMessage(currentState.getCurrentUnit(),endtile.getUnit());
                attackContainer.addAttack(message);
            }
            else
                {
                    System.out.println("攻击初级");
                    attack.startAttack(out,currentState,currentState.getCurrentUnit(),endtile);
                    MoveEnd();
                    return;
                }
        }
        else
        {
            path=getPath(starttile,endtile);
        }
        if(currentState.getCurrentUnit().getMoveScopeMode()==2) {
            currentState.setCanAttack(true);
            System.out.println(currentState.isCanAttack());
            int c = 0;
            for (Tile ti : attackTiles) {
                if (endtile == ti) {
                    c = 1;
                }
            }
            if (c == 1) {
                //if(c==1&&((path.size()==2)||(path.size()==3))){
                System.out.println("在被嘲讽的状态下攻击成功");
                System.out.println("路径path：" + path.size());
                System.out.println("目前能否攻击：" + currentState.isCanAttack());
                ((Attack) ObjectIOC.getInstance().getBean("Attack")).startAttack(out, currentState, currentState.getCurrentUnit(), endtile);
                //attack without move
                //Attack.getInstance().startAttack(out,currentState,endtile);
                MoveEnd();
            } else {
                System.out.println("在被嘲讽的状态下攻击失败");
                System.out.println(path.size());
                System.out.println(currentState.isCanAttack());
                BasicCommands.addPlayer1Notification(out, "you cannnot move because of provoke", 3);

                MoveFiled();
            }
        }
        else {
            if (path.size() <= 0)//can not touch enemy
            {
                BasicCommands.addPlayer1Notification(out, "No Way", 1);
                if(currentState.getCurrentUnit().isIsmove())
                {

                }else
                {
                    MoveFiled();
                }

            }
            else if (path.size() <= 1 && attackContainer.getAttacker(currentState.getCurrentUnit().getId()) != null && path.size() > 0)
            {
                System.out.println("1111111111111111111111111111");
                attack.startAttack(out, currentState, currentState.getCurrentUnit(), endtile);
                MoveEnd();
            }
          else if(path.size()<=2&&path.size()>0)
           {
            starttile.setUnit(null);
            currentState.getCurrentUnit().setTile(path.get(path.size()-1));
            path.get(path.size()-1).setUnit(currentState.getCurrentUnit());
            MoveMessage message=new MoveMessage(currentState.getCurrentUnit(),path.get(1),out,true);;
            unitMoveSender.AddMoveMessage(message);
            currentState.getCurrentUnit().setIsmove(true);
            //BasicCommands.moveUnitToTile(out,currentState.getCurrentUnit(),path.get(1));
           // try{Thread.sleep(100);}catch (Exception e) { }
           }
        else if(path.size()>2)
            {
                starttile.setUnit(null);
                path.get(path.size()-1).setUnit(currentState.getCurrentUnit());
                for(int i=0;i<path.size();i++)
                {
                    if(i==path.size()-1)
                    {

                        MoveMessage message=new MoveMessage(currentState.getCurrentUnit(),path.get(i),out,true);
                        unitMoveSender.AddMoveMessage(message);
                    }
                    else

                        {
                            MoveMessage message=new MoveMessage(currentState.getCurrentUnit(),path.get(i),out,false);
                            unitMoveSender.AddMoveMessage(message);
                        }
                    currentState.getCurrentUnit().setTile(path.get(i));
                }

                currentState.getCurrentUnit().setIsmove(true);
            }
        }
       //endTile has an opponent, human starts attack
        /**if(endtile.getUnit()!=null){
            Attack.getInstance().startAttack(out,gameState,starttile,endtile);
        }*/
        openlist.clear();
        closelist.clear();
        path.clear();
        for (Tile tile:totalTiles)
        {
            tile.setParent(null);
        }
        System.out.println(totalTiles.size());
        totalTiles.clear();

    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    /** highlight move range
     * param mode:player or ai*/
    public ArrayList<Tile> GetMoveScope(ActorRef out,Tile starttile,int mode)
    {
        if(mode==0)
        {
            if(!currentState.getHumanCurrentUnits().contains(starttile.getUnit()))
            {
                currentState.setCurrentUnit(null);
                return null;
            }
            else
            {
                currentState.setCurrentUnit(starttile.getUnit());
            }

        }
        else
        {

            if(!currentState.getAiCurrentUnits().contains(starttile.getUnit()))
            {
                currentState.setCurrentUnit(null);
                return null;
            }
            else
                {
                    currentState.setCurrentUnit(starttile.getUnit());
                }
        }

        if(currentState.getCurrentUnit().isIsmove()&&currentState.getCurrentUnit().isIsattack())
        {

            currentState.setCurrentUnit(null);
            BasicCommands.addPlayer1Notification(out,"This unit has moved and attacked",1);
            return null;
        }
        else if(currentState.getCurrentUnit().isIsattack())
        {

            currentState.setCurrentUnit(null);
            BasicCommands.addPlayer1Notification(out,"This unit has attacked ",1);
            return null;
        }
        else if(currentState.getCurrentUnit().isIsmove()&&!currentState.getCurrentUnit().isIsattack())
        {
            if(starttile.getUnit().getMoveScopeMode() ==2){
                totalTiles.clear();
                attackTiles.clear();
                Tile tile ;
                int tilex = starttile.getUnit().getTile().getTilex();
                int tiley = starttile.getUnit().getTile().getTiley();
                for (int i = tilex - 1; i <= tilex + 1; i++) {
                    if (i < 0 || i > 8) {
                        continue;
                    }

                    for (int j = tiley - 1; j <= tiley + 1; j++) {
                        if (j < 0 || j > 4) {
                            continue;
                        }
                        tile = currentState.getBoard().getTileByBoard(i, j);
                        if(mode==0) {
                            if ((tile.getUnit() != null) && ((tile.getUnit().getId() ==26) || (tile.getUnit().getId() == 27))) {
                                //if ((tile.getUnit().getId() == 26) || (tile.getUnit().getId() == 27)) {

                                attackTiles.add(tile);

                            }
                        }else{
                            //if(i!=tilex&&j!=tiley) {
                            if ((tile.getUnit() != null) && ((tile.getUnit().getId() == 17) || (tile.getUnit().getId() == 16)|| (tile.getUnit().getId() == 12)|| (tile.getUnit().getId() == 13))) {
                                //if ((tile.getUnit().getId() == 26) || (tile.getUnit().getId() == 27)) {

                                attackTiles.add(tile);
                            }
                            //  }
                        }
                    }
                }
                //attackTiles=totalTiles;
                currentState.setCurrentTiles(totalTiles,attackTiles);
                if(mode==0) {
                    System.out.println("shit i am provoke");
                    BasicCommands.addPlayer1Notification(out, "Shit Provoke!Now i can only attack!", 3);
                }else{
                    System.out.println("shit ai is provoke");
                    BasicCommands.addPlayer1Notification(out, "Now you can only attack because i provoke!", 3);
                }
                System.out.println("getmovescope");
                try{Thread.sleep(50);}catch(Exception e){}
                if(mode==0){
                    for(Tile t:attackTiles)
                    {
                        BasicCommands.drawTile(out,t,2);
                        try{Thread.sleep(3);}catch(Exception e){}
                    }
                }
                return  totalTiles;
            }
            totalTiles.clear();
            attackTiles.clear();
            System.out.println("tttttttttttttttttttttttt");
            ShowAttackScope(out,starttile,mode);
            if(attackTiles.size()<=0)
            {
                BasicCommands.addPlayer1Notification(out,"There is no enemy ",1);
                currentState.setCurrentUnit(null);
            }
            return null;
        }
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyy");
        int startX=starttile.getTilex();
        int startY=starttile.getTiley();
        totalTiles.clear();
        // MoveScopeMode 0 : common move
        if(starttile.getUnit().getMoveScopeMode() ==0){
            for(int i=startX-1;i<=startX+1;i++)
            {
                for(int j=startY-1;j<=startY+1;j++)
                {
                    if(canReach(i,j))totalTiles.add(currentState.getBoard().getTiles()[i][j]);
                }
            }
            if(canReach(startX+2,startY))totalTiles.add(currentState.getBoard().getTiles()[startX+2][startY]);
            if(canReach(startX-2,startY))totalTiles.add(currentState.getBoard().getTiles()[startX-2][startY]);
            if(canReach(startX,startY+2))totalTiles.add(currentState.getBoard().getTiles()[startX][startY+2]);
            if(canReach(startX,startY-2))totalTiles.add(currentState.getBoard().getTiles()[startX][startY-2]);
        }

        // MoveScopeMode 1 :fly move
        if(starttile.getUnit().getMoveScopeMode() ==1) {
            Tile tile;
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 5; y++) {
                    tile = currentState.getBoard().getTileByBoard(x, y);
                    if (canReach(x, y)) {
                        totalTiles.add(tile);
                    }
                }
            }
        }
        if(starttile.getUnit().getMoveScopeMode() ==2){
            totalTiles.clear();
            attackTiles.clear();
            Tile tile ;
            int tilex = starttile.getUnit().getTile().getTilex();
            int tiley = starttile.getUnit().getTile().getTiley();
            for (int i = tilex - 1; i <= tilex + 1; i++) {
                if (i < 0 || i > 8) {
                    continue;
                }

                for (int j = tiley - 1; j <= tiley + 1; j++) {
                    if (j < 0 || j > 4) {
                        continue;
                    }
                    tile = currentState.getBoard().getTileByBoard(i, j);
                    if(mode==0) {
                        if ((tile.getUnit() != null) && ((tile.getUnit().getId() ==26) || (tile.getUnit().getId() == 27))) {
                            //if ((tile.getUnit().getId() == 26) || (tile.getUnit().getId() == 27)) {

                            attackTiles.add(tile);

                        }
                    }else{
                        //if(i!=tilex&&j!=tiley) {
                        if ((tile.getUnit() != null) && ((tile.getUnit().getId() == 17) || (tile.getUnit().getId() == 16)|| (tile.getUnit().getId() == 12)|| (tile.getUnit().getId() == 13))) {
                            //if ((tile.getUnit().getId() == 26) || (tile.getUnit().getId() == 27)) {

                            attackTiles.add(tile);
                        }
                        //  }
                    }
                }
            }
            //attackTiles=totalTiles;
            currentState.setCurrentTiles(totalTiles,attackTiles);
            if(mode==0) {
                System.out.println("shit i am provoke");
                BasicCommands.addPlayer1Notification(out, "Shit Provoke!Now i can only attack!", 3);
            }else{
                System.out.println("shit ai is provoke");
                BasicCommands.addPlayer1Notification(out, "Now you can only attack because i provoke!", 3);
            }
            System.out.println("getmovescope");
            try{Thread.sleep(50);}catch(Exception e){}
            if(mode==0){
                for(Tile t:attackTiles)
                {
                    BasicCommands.drawTile(out,t,2);
                    try{Thread.sleep(3);}catch(Exception e){}
                }
            }
            return  totalTiles;
        }
        ArrayList<Tile>finalScope=new ArrayList<>();
        for(int i=0;i<totalTiles.size();i++){
            ArrayList<Tile> temppath=getPath(starttile,totalTiles.get(i));
            openlist.clear();
            closelist.clear();
            for (Tile tile:totalTiles)
            {
                tile.setParent(null);
            }
            if(temppath.size()>0){
                finalScope.add(totalTiles.get(i));
            }
        }
        totalTiles.clear();
        for(Tile tile:finalScope)
        {
            totalTiles.add(tile);
        }
        ShowAttackScope(out,starttile,mode);
        currentState.setCurrentTiles(totalTiles,attackTiles);
        try{Thread.sleep(50);}catch(Exception e){}
        if(mode==0){
            for(Tile tile:totalTiles)
            {
                BasicCommands.drawTile(out,tile,1);
                try{Thread.sleep(1);}catch(Exception e){}
            }
        }
        return totalTiles;
    }
    private ArrayList<Tile> getPath(Tile starttile,Tile endtile)
    {
        Tile parent=findPath(starttile,endtile);
        ArrayList<Tile>pathTile=new ArrayList<>();
        while(parent!=null)
        {
            pathTile.add(parent);
            parent=parent.getParent();
        }
        Collections.reverse(pathTile);
        return pathTile;
    }
    private Tile findMinTileInOpenList()
    {
        Tile tempTile=openlist.get(0);
        for(Tile tile:openlist)
        {
            if(tile.F< tempTile.F) tempTile=tile;
        }
        return  tempTile;
    }
    private ArrayList<Tile>findNeighborTile(Tile currenttile)
    {
        ArrayList<Tile> findNeighborTile=new ArrayList<>();
        int topX=currenttile.getTilex();
        int topY=currenttile.getTiley()-1;
        if(containInScope(topX,topY)&&!exists(closelist,topX,topY))
        {
            findNeighborTile.add(currentState.getBoard().getTileByBoard(topX,topY));
        }
        int bottomX=currenttile.getTilex();
        int bottomY=currenttile.getTiley()+1;
        if(containInScope(bottomX,bottomY)&&!exists(closelist,bottomX,bottomY))
        {
            findNeighborTile.add(currentState.getBoard().getTileByBoard(bottomX,bottomY));
        }
        int leftX=currenttile.getTilex()-1;
        int leftY=currenttile.getTiley();
        if(containInScope(leftX,leftY)&&!exists(closelist,leftX,leftY))
        {
            findNeighborTile.add(currentState.getBoard().getTileByBoard(leftX,leftY));
        }
        int rightX=currenttile.getTilex()+1;
        int rightY=currenttile.getTiley();
        if(containInScope(rightX,rightY)&&!exists(closelist,rightX,rightY))
        {
            findNeighborTile.add(currentState.getBoard().getTileByBoard(rightX,rightY));
        }
        return findNeighborTile;
    }
    private Tile findPath(Tile starTile,Tile endTile)
    {
        openlist.add(starTile);
        if(endTile.equals(starTile))
        {
            return starTile;
        }
        while(openlist.size()>0)
        {
            Tile currentTile=findMinTileInOpenList();
            openlist.remove(currentTile);
            closelist.add(currentTile);
            ArrayList<Tile> neighborTiles=findNeighborTile(currentTile);
            for(Tile tile:neighborTiles)
            {
                if(exists(openlist,tile))
                {
                    foundTile(currentTile,tile);
                }else
                    {
                        notFoundTile(currentTile,endTile,tile);
                    }
            }
            Tile tempTile=find(openlist,endTile);
            if(tempTile!=null)return tempTile;
        }
        return null;

    }
    private Tile find(List<Tile>tiles,Tile tile)
    {
        for(Tile atile:tiles)
        {
            if((atile.getTilex()==tile.getTilex())&&(atile.getTiley()==tile.getTiley())) return atile;
        }
        return null;
    }
    private void foundTile(Tile tempStart,Tile tile)
    {
        int G=calcG(tile);
        if(G<tile.G)
        {
            tile.setParent(tempStart);
            tile.G=G;
            tile.calcF();
        }
    }
    private void notFoundTile(Tile start,Tile end,Tile temp)
    {
        temp.setParent(start);
        temp.G=calcG(temp);
        temp.H=calcH(end,temp);
        temp.calcF();;
        openlist.add(temp);
    }
    private int calcG(Tile tile)
    {
        int G=STEP;
        int parentG=(tile.getParent()!=null?tile.getParent().G:0);
        return  G+parentG;
    }
    private int calcH(Tile endtile,Tile tile)
    {
        int step=Math.abs(tile.getTilex()-endtile.getTilex())+Math.abs(tile.getTiley()-endtile.getTiley());
        return step*STEP;
    }
    //check that if the rage is out of the limited rage
    private boolean containInScope(int x,int y)
    {
        for(Tile tile:totalTiles)
        {
            if(tile.getTilex()==x&&tile.getTiley()==y)return  true;
        }
        return false;
    }
    private boolean canReach(int x, int y)
    {
        if(x>=0&&y>=0&&x<currentState.getBoard().getTiles().length&&
                y<currentState.getBoard().getTiles()[0].length&&
                currentState.getBoard().getTiles()[x][y].getUnit()==null)
        {
            return true;
        }
        return  false;
    }
    private boolean exists(List<Tile> tiles, int x , int y)
    {
        for(Tile tile:tiles)
        {
            if((tile.getTilex()==x)&&(tile.getTiley()==y))return  true;
        }
        return  false;
    }
    private boolean exists(List<Tile> tiles,Tile tile)
    {
        if(tiles.contains(tile))return true;
        return  false;
    }
    private void MoveBegin()
    {
        if(totalTiles.size()>0)
        {
            for (Tile tile:totalTiles)
            {
                tile.setParent(null);
            }
            totalTiles.clear();
        }

    }
    public void MoveFiled()
    {
        if(currentState.getCurrentUnit()!=null) {
            currentState.getCurrentUnit().setIsmove(false);
            currentState.setCurrentUnit(null);
        }
    }
    public void MoveEnd()
    {
        if(currentState.getCurrentUnit()!=null) {
            currentState.setCurrentUnit(null);
        }
        this.setMoving(false);
        unitMoveSender.MoveSignal();
    }
    public void Initialize()
    {
        currentState.setCurrentUnit(null);
    }
    public ArrayList<Tile>FindBestPath (Tile startTile,Tile endtile)
    {
        int StartTilex=endtile.getTilex()-1>0?endtile.getTilex()-1:0;
        int EndTilex=endtile.getTilex()+1<currentState.getBoard().getTiles().length?endtile.getTilex()+1:currentState.getBoard().getTiles().length;
        int StartTileY=endtile.getTiley()-1>0?endtile.getTiley()-1:0;
        int EndTiley=endtile.getTiley()+1<currentState.getBoard().getTiles()[0].length?endtile.getTiley()+1:currentState.getBoard().getTiles()[0].length;
        List<Tile>ConerPath=new ArrayList<>();
        for(int i=StartTilex;i<=EndTilex;i++)
        {
            for(int j=StartTileY;j<=EndTiley;j++)
            {
               if(totalTiles.contains(currentState.getBoard().getTileByBoard(i,j))||currentState.getCurrentUnit().getTile().equals(currentState.getBoard().getTileByBoard(i,j)))
               {
                   ConerPath.add(currentState.getBoard().getTileByBoard(i,j));
               }
               else
                   {
                       continue;
                   }
            }
        }
        ArrayList<Tile>bestFinalPath=new ArrayList<>();
        for(Tile tile:ConerPath)
        {
            ArrayList<Tile>bestPath=getPath(startTile,tile);
            System.out.println("bestpathsdasdasdas"+ConerPath.size());
            openlist.clear();
            closelist.clear();
            for (Tile tiles:totalTiles)
            {
                tiles.setParent(null);
            }
            if(bestPath.size()<bestSize)
            {
                bestSize=bestPath.size();
                bestFinalPath=bestPath;
            }
        }
        System.out.println(bestFinalPath.size());
        bestSize=10;
        return  bestFinalPath;
    }
    /**Show attack scope
     * param:mode 0 Human 1 AI*/
    public ArrayList<Tile> ShowAttackScope(ActorRef out,Tile currentTile,int mode)
    {
        attackTiles.clear();
        if(mode==0)
        {
            for(Unit unit:currentState.getAiCurrentUnits())
            {
                //common attack
                if(currentTile.getUnit().getAttackScopeMode() == 0){
                    if(totalTiles.size()>0)
                    {
                        for(int i=0;i<totalTiles.size();i++)
                        {
                            if((Math.abs(unit.getTile().getTilex()-totalTiles.get(i).getTilex())+Math.abs(unit.getTile().getTiley()-totalTiles.get(i).getTiley()))<=1
                            ||JudgeAttackAngle(currentTile,unit.getTile())||(Math.abs(unit.getTile().getTilex()-currentTile.getTilex())+Math.abs(unit.getTile().getTiley()-currentTile.getTiley()))<=1)
                            {
                                if(attackTiles.contains(unit.getTile()))
                                {
                                    continue;
                                }
                                attackTiles.add(unit.getTile());
                            }
                        }
                        System.out.println(attackTiles.size());
                    }
                    else
                        {
                            attackTiles.add(unit.getTile());
                        }
                    // project attack
                }else if(currentTile.getUnit().getAttackScopeMode() == 1){
                    if((Math.abs(unit.getTile().getTilex()-currentTile.getTilex())+Math.abs(unit.getTile().getTiley()-currentTile.getTiley()))<=9)
                    {
                        if(attackTiles.contains(unit.getTile()))
                        {
                            continue;
                        }
                        attackTiles.add(unit.getTile());
                    }
                }
            }
        }
        else
        {
            for(Unit unit:currentState.getHumanCurrentUnits())
            {
                //common attack
                if(currentTile.getUnit().getAttackScopeMode() == 0){
                    if(totalTiles.size()>0)
                    {
                        for(int i=0;i<totalTiles.size();i++)
                        {
                            if((Math.abs(unit.getTile().getTilex()-totalTiles.get(i).getTilex())+Math.abs(unit.getTile().getTiley()-totalTiles.get(i).getTiley()))<=1
                            ||JudgeAttackAngle(currentTile,unit.getTile())||(Math.abs(unit.getTile().getTilex()-currentTile.getTilex())+Math.abs(unit.getTile().getTiley()-currentTile.getTiley()))<=1)
                            {
                                if(attackTiles.contains(unit.getTile()))
                                {
                                    continue;
                                }
                                attackTiles.add(unit.getTile());
                            }
                        }
                        System.out.println(attackTiles.size()+"这里是attacktilestotal");
                    }
                    else
                        {

                            attackTiles.add(unit.getTile());
                            System.out.println(attackTiles.size()+"这里是attacktiles");
                        }
                }else if(currentTile.getUnit().getAttackScopeMode() == 1){
                    if((Math.abs(unit.getTile().getTilex()-currentTile.getTilex())+Math.abs(unit.getTile().getTiley()-currentTile.getTiley()))<=9)
                    {
                        attackTiles.add(unit.getTile());

                    }
                }
            }
        }
        if(currentState.getCurrentUnit().getAttackScopeMode()==1)
        {
            for(Tile tile:attackTiles)
            {
                DrawRed(out,tile);
            }
            return null;

        }
        ArrayList<Tile> tempTile=new ArrayList<>();
        System.out.println(attackTiles.size()+"ffffffffffffffffffffffffff");
       for(Tile tile:attackTiles)
       {
           if(FindBestPath(currentTile,tile).size()<=0)
           {
               tempTile.add(tile);
           }
       }
        System.out.println(attackTiles.size()+"ooooppppppppp"+tempTile.size());
       List<Tile>checkTiles= attackTiles.stream().filter((Tile tileInAttack)->!tempTile.contains(tileInAttack)).collect(Collectors.toList());
      attackTiles.clear();
      for(Tile tileInCheck:checkTiles)
      {
          attackTiles.add(tileInCheck);
      }  
        if(currentState.getMode() ==0){
            for(Tile tile:attackTiles)
            {
                BasicCommands.drawTile(out,tile,2);
            }
        }
        for(int i=0;i<attackTiles.size();i++)
        {
            System.out.println(attackTiles.size());
            System.out.println(attackTiles.get(i).getTilex()+" "+attackTiles.get(i).getTiley()+"thehthrshgargfdgrafarararfr");
        }
        return attackTiles;
    }

    public ArrayList<Tile> getTotalTiles() {
        return totalTiles;
    }
    public boolean JudgeAttackAngle(Tile startTile,Tile Endtile)
    {
        if(Math.abs(Endtile.getTiley()-startTile.getTiley())==1&&Math.abs(Endtile.getTilex()-startTile.getTilex())==1)
        {
            return  true;
        }
        return  false;
    }
    public void DrawRed(ActorRef out,Tile tile)
    {
        BasicCommands.drawTile(out,tile,2);
    }
}