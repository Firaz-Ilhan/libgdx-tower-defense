package com.tower.defense.wave;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tower.defense.enemy.Enemy;
import com.tower.defense.network.packet.client.PacketLifepoints;
import com.tower.defense.player.Player;

import java.util.Iterator;

public class RenderWave {

    public static void renderWave(Player player, Wave waveClass, Sound enemyKilledSound) {

        Array<Enemy> wave;

        if (player.getPlayer()) {
            wave = waveClass.waveLeft;
        } else {
            wave = waveClass.waveRight;
        }
        // with an iterator it goes through each wave step by step
        for (Iterator<Enemy> iter = wave.iterator(); iter.hasNext(); ) {
            Enemy enemy = iter.next();

            // distance added with each frame
            float positionAddAmount = enemy.getSpeed() / 25;

            Vector2 currentEnemyPosition = enemy.getPosition();

            // the next waypoint the enemy will move to
            Vector2 nextWantedWaypoint = enemy.nextWaypoint(player.getPlayer());

            // only if the enemy's current position isn't the same as the desired waypoint
            // it will move towards it based on which coordinate (x and/or y) is wrong
            if (currentEnemyPosition.y != nextWantedWaypoint.y) {
                if (currentEnemyPosition.y > nextWantedWaypoint.y) {
                    if (currentEnemyPosition.y - positionAddAmount < nextWantedWaypoint.y) {
                        currentEnemyPosition.y = nextWantedWaypoint.y;
                        enemy.setPosition(currentEnemyPosition);
                    } else {
                        currentEnemyPosition.y = enemy.getY() - positionAddAmount;
                        enemy.setPosition(currentEnemyPosition);
                    }
                } else {
                    if (currentEnemyPosition.y + positionAddAmount > nextWantedWaypoint.y) {
                        currentEnemyPosition.y = nextWantedWaypoint.y;
                        enemy.setPosition(currentEnemyPosition);
                    } else {
                        currentEnemyPosition.y = enemy.getY() + positionAddAmount;
                        enemy.setPosition(currentEnemyPosition);
                    }
                }
            } else if (currentEnemyPosition.x != nextWantedWaypoint.x) {
                if (currentEnemyPosition.x > nextWantedWaypoint.x) {
                    if (currentEnemyPosition.x - positionAddAmount < nextWantedWaypoint.x) {
                        currentEnemyPosition.x = nextWantedWaypoint.x;
                        enemy.setPosition(currentEnemyPosition);
                    } else {
                        currentEnemyPosition.x = enemy.getX() - positionAddAmount;
                        enemy.setPosition(currentEnemyPosition);
                    }
                } else {
                    if (currentEnemyPosition.x + positionAddAmount > nextWantedWaypoint.x) {
                        currentEnemyPosition.x = nextWantedWaypoint.x;
                        enemy.setPosition(currentEnemyPosition);
                    } else {
                        currentEnemyPosition.x = enemy.getX() + positionAddAmount;
                        enemy.setPosition(currentEnemyPosition);
                    }
                }

                // if the positions of enemy and waypoint are the same
                // the wavePattern will progress to the next waypoint
            } else {
                enemy.advancePattern();
            }

            // if an enemy reaches the bottom edge of the map it gets
            // removed and the player looses health points based
            // on the enemy's damage
            if (enemy.getY() < -10) {
                if (player.getPlayer()) {
                    player.reduceLifepoints(enemy.getDamage());
                    //NETWORKING
                    if (waveClass.getGame().getClient() != null) {
                        waveClass.getGame().getClient().sendPacket(new PacketLifepoints(player.getLifepoints()));
                    }
                    //
                    waveClass.enemyPassed();
                }
                iter.remove();
            }

            // if the lifepoints of an enemy are reduced
            // to 0 it get's removed
            if (enemy.getLifepoints() <= 0) {
                iter.remove();
                enemyKilledSound.play();
            }
        }
    }
}
