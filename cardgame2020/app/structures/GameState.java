package structures;

import aiaction.AIAction;
import gamelogic.LoadCards;
import org.checkerframework.checker.units.qual.C;
import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 *
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {
    public int number=50;
    // store human player card
    private ArrayList<Card> player1Deck;
    //store aiPlayer card
    private ArrayList<Card> aiPlayerDeck;
    // store cards in player's hand
    private ArrayList<Card> p1CardsInHands;
    //store cards in aiPlayer's hand
    private ArrayList<Card> aiCardsInHands;
    // store player and aiPlayer
    private Player aiPlayer;
    private Player humanPlayer;
    // store HumanUnit in board
    private Unit humanAvatar;
    // store AiUnit in board
    private Unit aiAvatar;
    //store current units
    private ArrayList<Unit> humanCurrentUnits;
    private ArrayList<Unit> aiCurrentUnits;
    // store highlighting tiles, available tiles
    private ArrayList<Tile> currentTiles;
    //use it to move
    private Unit currentUnit;
    // store selected card
    private Card currentCard;
    //after accept the move stopped message this should be true.
    private volatile boolean canAttack=false;
    //number of turns
    private int turns;
    //mode: 0:human 1:AI
    private int mode;
    //board
    volatile Board board;



    public GameState(){
        //new board
        board = new Board();
        turns=1;
        //load card
        LoadCards.loadDeckOfCard(this);
        this.currentCard = new Card();
        //this should be 7 because We need to check that if the position of hand card is 6 ,this card should be deleted
        p1CardsInHands=new ArrayList<>(7);
        aiCardsInHands=new ArrayList<>(7);
        //load human
        this.humanAvatar = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
        this.humanPlayer=new Player(20,2);
        this.humanCurrentUnits = new ArrayList<>();
        // load humanUnit
        this.aiAvatar = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 1, Unit.class);
        this.aiPlayer=new Player(20,2);
        this.aiCurrentUnits = new ArrayList<>();
        this.currentTiles = new ArrayList<>();

        //set mode to 0, human player plays first
        mode=0;
    }

    public ArrayList<Card> getP1CardsInHands() {
        return p1CardsInHands;
    }

    public Unit getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(Unit currentUnit) {
        this.currentUnit = currentUnit;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }


    /**
     * mode=0 human draws a card, mode=1 ai draws a card
     * @param mode
     * @return
     */
    public Card drawCard(int mode){
        Random r=new Random();
        while (true){
            // generate card id ranging from 0-19
            int id = r.nextInt(20);
            // use the id to find the card in the list
            Card card = findCard(id,mode);
            // if card is found, then return, otherwise
            // regenerate the card id, and find the card again.
            if(card!=null){
                return card;
            }
        }
    }

    private Card findCard(int id,int mode){
        if(mode==0) {// mode=0-> it's human's turn
            for (int i = 0; i < player1Deck.size(); i++) {
                Card card = player1Deck.get(i);
                if (card.getId() == id) {
                    // if the card is found, then remove the card
                    // from the list, then return the card.
                    player1Deck.remove(card);
                    return card;
                }
            }
        }else {
            for(int j=0;j<aiPlayerDeck.size();j++){
                Card card=aiPlayerDeck.get(j);
                if(card.getId()==id){
                    aiPlayerDeck.remove(card);
                    return card;
                }
            }
        }
        // card not found
        return null;
    }
    /**
     * add cards to human's hand or ai's hand
     * @param card
     * @return if the cards more than 6return false.
     */
    public boolean addCardsToHands(Card card,int mode){
        if(mode==0) {//in human player mode
            p1CardsInHands.add(card);
            if (p1CardsInHands.size() >= 7) {
                return false;
            }
        }else {//in ai mode
            aiCardsInHands.add(card);
            if(aiCardsInHands.size()>=7){
                return false;
            }
        }
        return true;
    }
    public boolean checkCardsInHand(int mode)
    {
        if(mode==0) {
            if (p1CardsInHands.size() == 6) {
                return false;
            }
        }else {
            if(aiCardsInHands.size() == 6){
                return false;
            }
        }
        return true;
    }
    /**delete cards in hands
     * @param card
     * @return boolean*/
    public boolean deleteCardsInHands(Card card,int mode)
    {
        if(mode==0) {
            if (p1CardsInHands.contains(card)) {
                p1CardsInHands.remove(card);
                return true;
            }
        }else {
            if(aiCardsInHands.contains(card)){
                aiCardsInHands.remove(card);
                return true;
            }
        }
        return  false;
    }


    public void switchMode(){
        if(this.mode==0){
            this.mode=1;
            AIAction.getInstance().actionSignal();
        }else {
            this.mode=0;
        }
    }
    public Unit FindUnitByID(int index)
    {
        for(Unit unit:humanCurrentUnits)
        {
            if(unit.getId()==index)
                return unit;
        }
        for(Unit unit:aiCurrentUnits)
        {
            if(unit.getId()==index)
                return unit;
        }
        return null;
    }
    public int getMode() {
        return mode;
    }

    public ArrayList<Card> getPlayer1Deck() {
        return player1Deck;
    }

    public Player getHumanPlayer(){
        return humanPlayer;
    }

    public Unit getHumanAvatar() {
        return humanAvatar;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public ArrayList<Tile> getCurrentTiles() {
        return currentTiles;
    }

    public void setCurrentTiles(ArrayList<Tile> currentTiles) {
        this.currentTiles = currentTiles;
    }
    public void setCurrentTiles(ArrayList<Tile> currentTiles1,ArrayList<Tile> currentTiles2)
    {
        for(Tile tile:currentTiles1)
        {
            this.currentTiles.add(tile);
        }
        for(Tile tiles:currentTiles2)
        {
            this.currentTiles.add(tiles);
        }
    }

    public void setPlayer1Deck(ArrayList<Card> player1Deck) {
        this.player1Deck = player1Deck;
    }


    public ArrayList<Card> getAiPlayerDeck() {
        return aiPlayerDeck;
    }

    public void setAiPlayerDeck(ArrayList<Card> aiPlayerDeck) {
        this.aiPlayerDeck = aiPlayerDeck;
    }

    public ArrayList<Card> getAiCardsInHands() {
        return aiCardsInHands;
    }


    public Player getAiPlayer() {
        return aiPlayer;
    }

    public ArrayList<Unit> getHumanCurrentUnits() {
        return humanCurrentUnits;
    }

    public void setHumanCurrentUnits(Unit unit) {
        this.humanCurrentUnits.add(unit);
    }

    public ArrayList<Unit> getAiCurrentUnits() {
        return aiCurrentUnits;
    }

    public void setAiCurrentUnits(Unit unit) {
        this.aiCurrentUnits.add(unit);
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }
    public void addTurn(){
        turns=turns+1;
    }

    public Unit getAiAvatar() {
        return aiAvatar;
    }
    public void setAiAvatar(Unit aiAvatar) {
        aiAvatar = aiAvatar;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
