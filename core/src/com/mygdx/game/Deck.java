package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

public class Deck {
    private Array<Card> cards;

    public Deck() {
        cards = new Array<Card>(52);

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
               cards.add(new Card(suit, rank));
            }
        }

        cards.shuffle();
    }

    public Card draw() {
        if (cards.size == 0) {
            return null;
        } else {
            return cards.pop();
        }
    }

    public int cardsLeft() {
        return cards.size;
    }
}
