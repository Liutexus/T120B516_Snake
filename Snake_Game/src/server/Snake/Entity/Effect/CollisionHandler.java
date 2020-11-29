package server.Snake.Entity.Effect;

import server.Snake.Entity.AbstractMovingEntity;
import server.Snake.Entity.Entity;
import server.Snake.Entity.Snake;
import server.Snake.Enumerator.EEffect;

public class CollisionHandler {

    public static boolean checkCollisionWithTerrain(AbstractMovingEntity entity, int[][] terrain){
        int tPosX = (int)entity.getPositionX()+(int)entity.getVelocityX();
        int tPosY = (int)entity.getPositionY()+(int)entity.getVelocityY();
        try{
            if(terrain[tPosY][tPosX] == 6 && !entity.getEffects().containsKey(EEffect.STUN))  // '6' is an index for "Wall"
                return true;
        } catch (Exception e){
            if(e instanceof ArrayIndexOutOfBoundsException) return false; // Player is out of map
            e.printStackTrace();
        }
        return false;
    }

    // Check collisions with two entities
    public static boolean checkCollisionOnEntities(Entity lhs, Entity rhs){
        float lhsPositionX = lhs.getPositionX();
        float lhsPositionY = lhs.getPositionY();
        float lhsVelocityX;
        float lhsVelocityY;

        float rhsPositionX = rhs.getPositionX();
        float rhsPositionY = rhs.getPositionY();

        // Are both entities are one on top of each other?
        if(lhsPositionX == rhsPositionX && lhsPositionY == rhsPositionY)
            return true;

        // Is 'lhs' a moving entity?
        if(lhs instanceof AbstractMovingEntity){
            lhsVelocityX = ((AbstractMovingEntity) lhs).getVelocityX();
            lhsVelocityY = ((AbstractMovingEntity) lhs).getVelocityY();

            // Is 'lhs' with velocity colliding with 'rhs'?
            if(lhsPositionX + lhsVelocityX == rhsPositionX && lhsPositionY + lhsVelocityY == rhsPositionY)
                return true;
            }

        return false;
    }

    // Checking collisions with the tail
    public static int checkCollisionOnTails(Snake lhs, Snake rhs){
        float lhsPositionX = lhs.getPositionX();
        float lhsPositionY = lhs.getPositionY();

        int rhsTailLength = rhs.getTailLength();

        for (int i = 0; i < rhsTailLength; i++){
            if(rhs.getPreviousPositionsX().size() <= i) return 0;
            try{
                if(lhsPositionX == (float)rhs.getPreviousPositionsX().get(i) && lhsPositionY == (float)rhs.getPreviousPositionsY().get(i))
                    return i;
            } catch (Exception e){
                return 0;
            }
        }
        return 0;
    }




}
