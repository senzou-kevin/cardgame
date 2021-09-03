package commands;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;
import java.util.ArrayList;

/**
 *This class calculates the scope of Summon and Spell
 *  and highlights the Tiles.
 * */
public class HighlightScope {

    public HighlightScope() {
    }

    /**
     * This method will highlight the selected card and summon Tiles,
     * store clickedCard and summon Tiles in the GameState
     *
     * summonScopeMode 0 : common scope
     * summonScopeMode 1 : fly scope
     * */
    public void highlightUnitCard(ActorRef out, GameState gameState, Card card, int handPosition, ArrayList<Unit> unitsOnBoard){
        ArrayList<Tile> tiles = new ArrayList<>();
        //only human player can highlight card
        if(gameState.getMode()==0) {
            // Highlight Card
            BasicCommands.drawCard(out, card, handPosition, 1);
            try {Thread.sleep(10);} catch (Exception e){}
        }
        // Highlight Scope
        HighlightScope highlightScope = new HighlightScope();
        // Get summon scope
        if(card.getUnit().getSummonScopeMode() == 0){
            tiles = highlightScope.commonSummonScope(gameState,unitsOnBoard);
        }
        if(card.getUnit().getSummonScopeMode() == 1){
            tiles = highlightScope.flySummonScope(gameState);
        }
        if(gameState.getMode() == 0){
            // Highlight summon scope
            BasicCommands.drawScope(out,gameState,tiles,1);
        }
        // Store highlighting tiles in the gameSate
        gameState.setCurrentTiles(tiles);
        // Store the card which is currently selected in the gameSate
        gameState.setCurrentCard(card);
    }

    /**
     * summonScopeMode 0 : common scope
     * Based on the HumanPlayer's/ AI's Position, get 8 Tiles
     * There is no Unit in the Tile
     * */
    public ArrayList<Tile> commonSummonScope(GameState gameState, ArrayList<Unit>unitsOnBoard) {
        ArrayList<Tile> scope = new ArrayList<>();
        Tile tile = new Tile();
        // Get all units' tilex and tiley
        for(Unit unit:unitsOnBoard){
            int tilex = unit.getTile().getTilex();
            int tiley = unit.getTile().getTiley();
            for (int i = tilex-1; i <= tilex + 1; i++) {
                if(i<0||i>8) {
                    continue;
                };
                for (int j = tiley - 1; j <= tiley + 1; j++) {
                    if(j<0||j>4){
                        continue;
                    }
                    tile = gameState.getBoard().getTileByBoard(i,j);
                    //If a Unit does not exist on the current Tile, the Player can summon that Unit
                    if(tile.getUnit() == null){
                        scope.add(tile);
                    }
                }
            }
        }
        return scope;
    }

    /**
     * summonScopeMode 1 : fly scope
     * card : Windshrike  Planar Scout
     * Can be summoned anywhere on the board
     * */
    public ArrayList<Tile> flySummonScope(GameState gameState) {
        Tile tile = new Tile();
        ArrayList<Tile> scope = new ArrayList<>();
        // get all Tile
        for(int x = 0; x < 9;x++){
            for(int y =0; y < 5;y++){
                tile = gameState.getBoard().getTileByBoard(x,y);
                if(tile.getUnit() == null){
                    scope.add(tile);
                }
            }
        }
        return scope;
    }

    /**
     * spell scope
     * spellScopeMode 0 : get Ai's Unit
     * spellScopeMode 1 : get All Unit
     * */
    public ArrayList<Unit> getSpellScope(ActorRef out,GameState gameState,int kind){
        ArrayList<Unit> units = new ArrayList<>();
        // kind 0: get Ai's Units Tiles
        if(kind == 0){
            units = gameState.getAiCurrentUnits();
        }
        // kind 1: get All Units Tiles
        if (kind == 1){
            for(Unit u:gameState.getHumanCurrentUnits()){
                units.add(u);
            }
            for(Unit u:gameState.getAiCurrentUnits()){
                units.add(u);
            }
        }
        // kind 2: get AI's Avatar
        if(kind==2) {
            units.add(gameState.getAiAvatar());
        }
        // kind 3: get All Units Tiles
        if(kind==3) {
            units=gameState.getHumanCurrentUnits();
            units.addAll(gameState.getAiCurrentUnits());
        }

        return units;
    }
    /**
     * This method will highlight the Unit which can be selected by spellCard
     * kind 0 : Highlight Ai's Unit
     * kind 1 : Highlight All Unit
     * */
    public void highlightSpell(ActorRef out, GameState gameState,Card card,int handPosition,int kind){
        //only human can highlight card
        if(gameState.getMode()==0) {
            // Highlight Card
            BasicCommands.drawCard(out, card, handPosition, 1);
        }
        gameState.setCurrentCard(card);
        ArrayList<Tile> scope = new ArrayList<>();
        // highlight
        for(Unit unit:getSpellScope(out,gameState,kind)){
            //only human can highlight spell
            if(gameState.getMode()==0){
                BasicCommands.drawTile(out,unit.getTile(),2);
                try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
            }
            scope.add(unit.getTile());
        }
        gameState.setCurrentTiles(scope);
    }

}
