package gamelogic;

import akka.actor.ActorRef;
import annotation.IOCService;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Unit;

@IOCService(name = "EndTurn")
public class EndTurn {
    public void drawCard(ActorRef out, GameState gameState, int mode){
        if((mode==0&&gameState.getPlayer1Deck().size()==0)||(mode==1&&gameState.getAiPlayerDeck().size()==0)){
            if(mode==0){
                BasicCommands.addPlayer1Notification(out,"no cards, Human lost",3);
            }else {
                BasicCommands.addPlayer1Notification(out,"no cards, AI lost",3);
            }
            System.exit(0);
            return;
        }
        //draw a card from the deck
        Card card = gameState.drawCard(mode);
        //check whether cards in hands are over 6 or not
        if(mode==0) {
            if (gameState.addCardsToHands(card, mode)) {
                BasicCommands.drawCard(out, card, gameState.getP1CardsInHands().size(), 0);
            } else {
                BasicCommands.addPlayer1Notification(out, "More than 6 cards in hand", 2);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gameState.deleteCardsInHands(card, gameState.getMode());
            }
        }else {
            boolean flag=gameState.addCardsToHands(card,mode);
            if(!flag){
                gameState.deleteCardsInHands(card,mode);
            }
        }
    }

    public void setMana(ActorRef out,GameState gameState,int mode){
        if(mode==0) {
            gameState.getHumanPlayer().setMana(gameState.getTurns()+1);
            BasicCommands.setPlayer1Mana(out, gameState.getHumanPlayer());
        }else {
            gameState.getAiPlayer().setMana(gameState.getTurns()+1);
            BasicCommands.setPlayer2Mana(out,gameState.getAiPlayer());
        }
    }
    public void InitTurn(GameState gameState, int mode)
    {
        if(mode==0)
        {
            for(Unit unit:gameState.getHumanCurrentUnits())
            {
                unit.setIsattack(false);
                unit.setIsmove(false);
                unit.setCanAttack(false);
            }
        }
        else
            {
                for(Unit unit:gameState.getAiCurrentUnits())
                {
                    unit.setIsattack(false);
                    unit.setIsmove(false);
                    unit.setCanAttack(false);
                }
            }
    }
}
