package com.globalformulae.shiguang.model;

import java.util.List;

/**
 * Created by ZXG on 2017/2/27.
 */

public class NBAEvents {
    private String reason;
    private EventsResult result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public EventsResult getResult() {
        return result;
    }

    public void setResult(EventsResult result) {
        this.result = result;
    }

    public class EventsResult{
        private String title;
        private List<DayResult> list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<DayResult> getList() {
            return list;
        }

        public void setList(List<DayResult> list) {
            this.list = list;
        }

        public class DayResult{
            private String title;
            private List<Compatition> tr;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<Compatition> getTr() {
                return tr;
            }

            public void setTr(List<Compatition> tr) {
                this.tr = tr;
            }

            public class Compatition{
                private String time;
                private String player1;
                private String player2;
                private String player1logo;
                private String player2logo;
                private String player1logobig;
                private String player2logobig;
                private String status;
                private String score;

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getScore() {
                    return score;
                }

                public void setScore(String score) {
                    this.score = score;
                }

                public String getPlayer1() {
                    return player1;
                }

                public void setPlayer1(String player1) {
                    this.player1 = player1;
                }

                public String getPlayer2() {
                    return player2;
                }

                public void setPlayer2(String player2) {
                    this.player2 = player2;
                }

                public String getPlayer1logo() {
                    return player1logo;
                }

                public void setPlayer1logo(String player1logo) {
                    this.player1logo = player1logo;
                }

                public String getPlayer2logo() {
                    return player2logo;
                }

                public void setPlayer2logo(String player2logo) {
                    this.player2logo = player2logo;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getPlayer1logobig() {
                    return player1logobig;
                }

                public void setPlayer1logobig(String player1logobig) {
                    this.player1logobig = player1logobig;
                }

                public String getPlayer2logobig() {
                    return player2logobig;
                }

                public void setPlayer2logobig(String player2logobig) {
                    this.player2logobig = player2logobig;
                }
            }
        }
    }
}
