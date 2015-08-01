package com.mygdx.game;

public class Card {

    public enum Suit {
        HEARTS(0), SPADES(1), CLUBS(2), DIAMONDS(3);

        private final int value;

        private Suit(int _value) {
            value = _value;
        }

        int getValue() {
            return value;
        }
    }

    public enum Rank {
        ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13);

        private final int value;

        private Rank(int _value) {
            value = _value;
        }

        int getValue() {
            return value;
        }
    }

    private Suit suit;
    private Rank rank;

    public Card(Suit _suit, Rank _rank) {
        suit = _suit;
        rank = _rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }
}
