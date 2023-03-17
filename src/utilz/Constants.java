package utilz;

import Main.Game;

import javax.swing.plaf.PanelUI;

public class Constants {
    public static final float GRAVITY = 0.04f * Game.SCALE;

    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }
        public static class PauseButtons{
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
            public static final int URM_SIZE_DEFAULT = 56;
            public static final int URM_SIZE= (int) (URM_SIZE_DEFAULT * Game.SCALE);
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;
            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

    public static class Environment{
        public static final int PB1_WIDTH_DEFAULT = 832;
        public static final int PB1_HEIGHT_DEFAULT = 468;
        public static final int PB1_WIDTH = (int) (PB1_WIDTH_DEFAULT * Game.SCALE);
        public static final int PB1_HEIGHT = (int) (PB1_HEIGHT_DEFAULT * Game.SCALE);
    }

    public static class Direction{
        public static final int LEFT = 0;
        //public static final int UP = 1;
        public static final int RIGHT = 2;
        //public static final int DOWN = 3;
    }
    public static class PlayerConstants{
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int ATTACK_2 = 3;
        public static final int DEAD = 4;
        public static final int JUMPING = 5;
        public static final int FALLING = 6;

        public static int GetSpriteAmount(int player_action){
            switch (player_action){
                case IDLE -> {
                    return 8;
                }
                case RUNNING -> {
                    return 8;
                }
                case ATTACK -> {
                    return 14;
                }
                case ATTACK_2 -> {
                    return 6;
                }
                case DEAD -> {
                    return 19;
                }
                case JUMPING -> {
                    return 8;
                }
                case FALLING -> {
                    return 7;
                }
                default -> {
                    return 1;
                }
            }
        }

    }

    public static class EnemyConstants {
        public static final int ASSASSIN = 0;
        public static final int SPEARMAN = 1;
        public static final int BOSS = 2;
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int BOSS_RUNNING = 0;
        public static final int ATTACK = 2;
        public static final int BOSS_ATTACK_1 = 1;
        public static final int BOSS_ATTACK_2 = 2;
        public static final int BOSS_ATTACK_3 = 3;
        public static final int HIT = 3;
        public static final int DEAD = 4;
        public static final int ENEMY_1_WIDTH_DEFAULT = 64;
        public static final int ENEMY_1_HEIGHT_DEFAULT = 64;
        public static final int BOSS_WIDTH_DEFAULT = 89;
        public static final int BOSS_HEIGHT_DEFAULT = 76;

        public static final int ENEMY_1_WIDTH = (int) (ENEMY_1_WIDTH_DEFAULT * Game.SCALE);
        public static final int ENEMY_1_HEIGHT = (int) (ENEMY_1_HEIGHT_DEFAULT * Game.SCALE);
        public static final int BOSS_WIDTH = (int) (BOSS * Game.SCALE);
        public static final int BOSS_HEIGHT = (int) (BOSS_HEIGHT_DEFAULT * Game.SCALE);
        public static final int ENEMY_DRAWOFFSET_X = (int) (40 * Game.SCALE);
        public static final int ENEMY_DRAWOFFSET_Y = (int) (52 * Game.SCALE);


        public static int GetSpriteAmount(int enemy_type, int enemy_State){
            switch (enemy_type) {
                case ASSASSIN:
                    switch (enemy_State) {
                        case IDLE:
                            return 8;
                        case RUNNING:
                            return 8;
                        case ATTACK:
                            return 7;
                        case HIT:
                            return 1;
                        case DEAD:
                            return 19;
                        default:
                            return 1;
                    }
                case SPEARMAN:
                    switch (enemy_State) {
                        case IDLE:
                            return 8;
                        case RUNNING:
                            return 8;
                        case ATTACK:
                            return 16;
                        case HIT:
                            return 1;
                        case DEAD:
                            return 19;
                        default:
                            return 1;
                    }
                case BOSS:
                    switch (enemy_State) {
                        case BOSS_RUNNING:
                            return 8;
                        case BOSS_ATTACK_1, BOSS_ATTACK_2, BOSS_ATTACK_3:
                            return 10;
                        case DEAD:
                            return 10;
                        default:
                            return 1;
                    }
                }
                return 0;
        }
        public static int GetMaxHealth(int enemy_type) {
            switch (enemy_type) {
                case ASSASSIN:
                    return 25;
                case SPEARMAN:
                    return 55;
                case BOSS:
                    return 100;
                default:
                    return 1;
            }
        }

        public static int GetEnemyDmg(int enemt_type) {
            switch (enemt_type) {
                case ASSASSIN:
                    return 15;
                case SPEARMAN:
                    return 20;
                case BOSS:
                    return 20;
                default:
                    return 0;
            }
        }

    }

    public static class Objects {
        public static final int SPIKE = 0;
        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (Game.SCALE * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int) (Game.SCALE * SPIKE_HEIGHT_DEFAULT);

        public static final int RED_POTION = 1;
        public static final int BLUE_POTION = 2;
        public static final int BARREL = 3;
        public static final int BOX = 4;

        public static final int ARCHER_ATTACK_RIGHT = 5; // bắn từ trái qua phải
        public static final int ARCHER_ATTACK_LEFT = 6; // bắn từ phải qua trái
        public static final int ARCHER_DEATH_RIGHT = 7;
        public static final int ARCHER_DEATH_LEFT = 8;

        public static final int RED_POTION_VALUE = 20;
        public static final int BLUE_POTION_VALUE = 100;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (Game.SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int) (Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (Game.SCALE * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int) (Game.SCALE * POTION_HEIGHT_DEFAULT);

        public static final int ARCHER_WIDTH_DEFAULT = 64;
        public static final int ARCHER_HEIGHT_DEFAULT = 64;
        public static final int ARCHER_WIDTH = (int) (Game.SCALE * ARCHER_WIDTH_DEFAULT);
        public static final int ARCHER_HEIGHT = (int) (Game.SCALE * ARCHER_HEIGHT_DEFAULT);

        public static final int BULLET_WIDTH_DEFAULT = 24;
        public static final int BULLET_HEIGHT_DEFAULT = 24;
        public static final int BULLET_WIDTH = (int) (BULLET_WIDTH_DEFAULT * Game.SCALE);
        public static final int BULLET_HEIGHT = (int) (BULLET_HEIGHT_DEFAULT * Game.SCALE);
        public static final float SPEED = 0.5f * Game.SCALE;
        public static int GetSpriteAmount(int object_type) {
            switch (object_type) {
                case RED_POTION, BLUE_POTION:
                    return 7;
                case BARREL, BOX:
                    return 8;
                case ARCHER_ATTACK_LEFT, ARCHER_ATTACK_RIGHT, ARCHER_DEATH_LEFT, ARCHER_DEATH_RIGHT:
                    return 9;
            }
            return 1;
        }
    }
}
