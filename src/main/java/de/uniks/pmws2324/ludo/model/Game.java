package de.uniks.pmws2324.ludo.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Game
{
   public static final String PROPERTY_CURRENT_PLAYER = "currentPlayer";
   public static final String PROPERTY_ACTIVE = "active";
   public static final String PROPERTY_FIRST_ROUND = "firstRound";
   public static final String PROPERTY_FIRST_ROUND_INT = "firstRoundInt";
   private Player currentPlayer;
   protected PropertyChangeSupport listeners;
   private boolean active;
   private boolean firstRound;
   private int firstRoundInt;

   public Player getCurrentPlayer()
   {
      return this.currentPlayer;
   }

   public Game setCurrentPlayer(Player value)
   {
      if (Objects.equals(value, this.currentPlayer))
      {
         return this;
      }

      final Player oldValue = this.currentPlayer;
      this.currentPlayer = value;
      this.firePropertyChange(PROPERTY_CURRENT_PLAYER, oldValue, value);
      return this;
   }

   public boolean isActive()
   {
      return this.active;
   }

   public Game setActive(boolean value)
   {
      if (value == this.active)
      {
         return this;
      }

      final boolean oldValue = this.active;
      this.active = value;
      this.firePropertyChange(PROPERTY_ACTIVE, oldValue, value);
      return this;
   }

   public boolean isFirstRound()
   {
      return this.firstRound;
   }

   public Game setFirstRound(boolean value)
   {
      if (value == this.firstRound)
      {
         return this;
      }

      final boolean oldValue = this.firstRound;
      this.firstRound = value;
      this.firePropertyChange(PROPERTY_FIRST_ROUND, oldValue, value);
      return this;
   }

   public int getFirstRoundInt()
   {
      return this.firstRoundInt;
   }

   public Game setFirstRoundInt(int value)
   {
      if (value == this.firstRoundInt)
      {
         return this;
      }

      final int oldValue = this.firstRoundInt;
      this.firstRoundInt = value;
      this.firePropertyChange(PROPERTY_FIRST_ROUND_INT, oldValue, value);
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
}
