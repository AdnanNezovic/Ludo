package de.uniks.pmws2324.ludo.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class Player
{
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_NEXT_PLAYER = "nextPlayer";
   public static final String PROPERTY_COLOR = "color";
   public static final String PROPERTY_BASE_FIELDS = "baseFields";
   public static final String PROPERTY_DICE = "dice";
   public static final String PROPERTY_PIECES = "pieces";
   public static final String PROPERTY_WINNER = "winner";
   public static final String PROPERTY_PRIORITY = "priority";
   public static final String PROPERTY_HAS_ROLLED = "hasRolled";
   public static final String PROPERTY_START_FIELD = "startField";
   public static final String PROPERTY_CAN_MOVE_APIECE = "canMoveAPiece";
   private String name;
   private Player nextPlayer;
   private Color color;
   private List<Field> baseFields;
   private Dice dice;
   private List<Piece> pieces;
   protected PropertyChangeSupport listeners;
   private boolean winner;
   private int priority;
   private boolean hasRolled;
   private Field startField;
   private boolean canMoveAPiece;

   public String getName()
   {
      return this.name;
   }

   public Player setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      return this;
   }

   public Player getNextPlayer()
   {
      return this.nextPlayer;
   }

   public Player setNextPlayer(Player value)
   {
      if (Objects.equals(value, this.nextPlayer))
      {
         return this;
      }

      final Player oldValue = this.nextPlayer;
      this.nextPlayer = value;
      this.firePropertyChange(PROPERTY_NEXT_PLAYER, oldValue, value);
      return this;
   }

   public Color getColor()
   {
      return this.color;
   }

   public Player setColor(Color value)
   {
      if (Objects.equals(value, this.color))
      {
         return this;
      }

      final Color oldValue = this.color;
      this.color = value;
      this.firePropertyChange(PROPERTY_COLOR, oldValue, value);
      return this;
   }

   public List<Field> getBaseFields()
   {
      return this.baseFields != null ? Collections.unmodifiableList(this.baseFields) : Collections.emptyList();
   }

   public Player withBaseFields(Field value)
   {
      if (this.baseFields == null)
      {
         this.baseFields = new ArrayList<>();
      }
      if (!this.baseFields.contains(value))
      {
         this.baseFields.add(value);
         value.setBaseFieldsOwner(this);
         this.firePropertyChange(PROPERTY_BASE_FIELDS, null, value);
      }
      return this;
   }

   public Player withBaseFields(Field... value)
   {
      for (final Field item : value)
      {
         this.withBaseFields(item);
      }
      return this;
   }

   public Player withBaseFields(Collection<? extends Field> value)
   {
      for (final Field item : value)
      {
         this.withBaseFields(item);
      }
      return this;
   }

   public Player withoutBaseFields(Field value)
   {
      if (this.baseFields != null && this.baseFields.remove(value))
      {
         value.setBaseFieldsOwner(null);
         this.firePropertyChange(PROPERTY_BASE_FIELDS, value, null);
      }
      return this;
   }

   public Player withoutBaseFields(Field... value)
   {
      for (final Field item : value)
      {
         this.withoutBaseFields(item);
      }
      return this;
   }

   public Player withoutBaseFields(Collection<? extends Field> value)
   {
      for (final Field item : value)
      {
         this.withoutBaseFields(item);
      }
      return this;
   }

   public Dice getDice()
   {
      return this.dice;
   }

   public Player setDice(Dice value)
   {
      if (this.dice == value)
      {
         return this;
      }

      final Dice oldValue = this.dice;
      if (this.dice != null)
      {
         this.dice = null;
         oldValue.setCurrentPlayer(null);
      }
      this.dice = value;
      if (value != null)
      {
         value.setCurrentPlayer(this);
      }
      this.firePropertyChange(PROPERTY_DICE, oldValue, value);
      return this;
   }

   public List<Piece> getPieces()
   {
      return this.pieces != null ? Collections.unmodifiableList(this.pieces) : Collections.emptyList();
   }

   public Player withPieces(Piece value)
   {
      if (this.pieces == null)
      {
         this.pieces = new ArrayList<>();
      }
      if (!this.pieces.contains(value))
      {
         this.pieces.add(value);
         value.setPieceOwner(this);
         this.firePropertyChange(PROPERTY_PIECES, null, value);
      }
      return this;
   }

   public Player withPieces(Piece... value)
   {
      for (final Piece item : value)
      {
         this.withPieces(item);
      }
      return this;
   }

   public Player withPieces(Collection<? extends Piece> value)
   {
      for (final Piece item : value)
      {
         this.withPieces(item);
      }
      return this;
   }

   public Player withoutPieces(Piece value)
   {
      if (this.pieces != null && this.pieces.remove(value))
      {
         value.setPieceOwner(null);
         this.firePropertyChange(PROPERTY_PIECES, value, null);
      }
      return this;
   }

   public Player withoutPieces(Piece... value)
   {
      for (final Piece item : value)
      {
         this.withoutPieces(item);
      }
      return this;
   }

   public Player withoutPieces(Collection<? extends Piece> value)
   {
      for (final Piece item : value)
      {
         this.withoutPieces(item);
      }
      return this;
   }

   public boolean isWinner()
   {
      return this.winner;
   }

   public Player setWinner(boolean value)
   {
      if (value == this.winner)
      {
         return this;
      }

      final boolean oldValue = this.winner;
      this.winner = value;
      this.firePropertyChange(PROPERTY_WINNER, oldValue, value);
      return this;
   }

   public int getPriority()
   {
      return this.priority;
   }

   public Player setPriority(int value)
   {
      if (value == this.priority)
      {
         return this;
      }

      final int oldValue = this.priority;
      this.priority = value;
      this.firePropertyChange(PROPERTY_PRIORITY, oldValue, value);
      return this;
   }

   public boolean isHasRolled()
   {
      return this.hasRolled;
   }

   public Player setHasRolled(boolean value)
   {
      if (value == this.hasRolled)
      {
         return this;
      }

      final boolean oldValue = this.hasRolled;
      this.hasRolled = value;
      this.firePropertyChange(PROPERTY_HAS_ROLLED, oldValue, value);
      return this;
   }

   public Field getStartField()
   {
      return this.startField;
   }

   public Player setStartField(Field value)
   {
      if (Objects.equals(value, this.startField))
      {
         return this;
      }

      final Field oldValue = this.startField;
      this.startField = value;
      this.firePropertyChange(PROPERTY_START_FIELD, oldValue, value);
      return this;
   }

   public boolean isCanMoveAPiece()
   {
      return this.canMoveAPiece;
   }

   public Player setCanMoveAPiece(boolean value)
   {
      if (value == this.canMoveAPiece)
      {
         return this;
      }

      final boolean oldValue = this.canMoveAPiece;
      this.canMoveAPiece = value;
      this.firePropertyChange(PROPERTY_CAN_MOVE_APIECE, oldValue, value);
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

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getName());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutBaseFields(new ArrayList<>(this.getBaseFields()));
      this.setDice(null);
      this.withoutPieces(new ArrayList<>(this.getPieces()));
   }
}
