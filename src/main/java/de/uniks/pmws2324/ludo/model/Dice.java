package de.uniks.pmws2324.ludo.model;
import java.beans.PropertyChangeSupport;

public class Dice
{
   public static final String PROPERTY_NUMBER = "number";
   public static final String PROPERTY_CURRENT_PLAYER = "currentPlayer";
   private int number;
   private Player currentPlayer;
   protected PropertyChangeSupport listeners;

   public int getNumber()
   {
      return this.number;
   }

   public Dice setNumber(int value)
   {
      if (value == this.number)
      {
         return this;
      }

      final int oldValue = this.number;
      this.number = value;
      this.firePropertyChange(PROPERTY_NUMBER, oldValue, value);
      return this;
   }

   public Player getCurrentPlayer()
   {
      return this.currentPlayer;
   }

   public Dice setCurrentPlayer(Player value)
   {
      if (this.currentPlayer == value)
      {
         return this;
      }

      final Player oldValue = this.currentPlayer;
      if (this.currentPlayer != null)
      {
         this.currentPlayer = null;
         oldValue.setDice(null);
      }
      this.currentPlayer = value;
      if (value != null)
      {
         value.setDice(this);
      }
      this.firePropertyChange(PROPERTY_CURRENT_PLAYER, oldValue, value);
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   public void removeYou()
   {
      this.setCurrentPlayer(null);
   }
}
