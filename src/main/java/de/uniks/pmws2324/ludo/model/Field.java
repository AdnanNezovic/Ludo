package de.uniks.pmws2324.ludo.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Field extends Location {
   public static final String PROPERTY_NEXT_FIELD = "nextField";
   public static final String PROPERTY_PIECE = "piece";
   public static final String PROPERTY_BASE_FIELDS_OWNER = "baseFieldsOwner";
   public static final String PROPERTY_COLOR = "color";
   public static final String PROPERTY_NEXT_HOME_FIELD = "nextHomeField";
   public static final String PROPERTY_TYPE = "type";
   private Field nextField;
   private Piece piece;
   private Player baseFieldsOwner;
   private Color color;
   private Field nextHomeField;
   private String type;

   public Field getNextField()
   {
      return this.nextField;
   }

   public Field setNextField(Field value)
   {
      if (Objects.equals(value, this.nextField))
      {
         return this;
      }

      final Field oldValue = this.nextField;
      this.nextField = value;
      this.firePropertyChange(PROPERTY_NEXT_FIELD, oldValue, value);
      return this;
   }

   public Piece getPiece()
   {
      return this.piece;
   }

   public Field setPiece(Piece value)
   {
      if (this.piece == value)
      {
         return this;
      }

      final Piece oldValue = this.piece;
      if (this.piece != null)
      {
         this.piece = null;
         oldValue.setPieceField(null);
      }
      this.piece = value;
      if (value != null)
      {
         value.setPieceField(this);
      }
      this.firePropertyChange(PROPERTY_PIECE, oldValue, value);
      return this;
   }

   public Player getBaseFieldsOwner()
   {
      return this.baseFieldsOwner;
   }

   public Field setBaseFieldsOwner(Player value)
   {
      if (this.baseFieldsOwner == value)
      {
         return this;
      }

      final Player oldValue = this.baseFieldsOwner;
      if (this.baseFieldsOwner != null)
      {
         this.baseFieldsOwner = null;
         oldValue.withoutBaseFields(this);
      }
      this.baseFieldsOwner = value;
      if (value != null)
      {
         value.withBaseFields(this);
      }
      this.firePropertyChange(PROPERTY_BASE_FIELDS_OWNER, oldValue, value);
      return this;
   }

   public Color getColor()
   {
      return this.color;
   }

   public Field setColor(Color value)
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

   public Field getNextHomeField()
   {
      return this.nextHomeField;
   }

   public Field setNextHomeField(Field value)
   {
      if (Objects.equals(value, this.nextHomeField))
      {
         return this;
      }

      final Field oldValue = this.nextHomeField;
      this.nextHomeField = value;
      this.firePropertyChange(PROPERTY_NEXT_HOME_FIELD, oldValue, value);
      return this;
   }

   public String getType()
   {
      return this.type;
   }

   public Field setType(String value)
   {
      if (Objects.equals(value, this.type))
      {
         return this;
      }

      final String oldValue = this.type;
      this.type = value;
      this.firePropertyChange(PROPERTY_TYPE, oldValue, value);
      return this;
   }

   public void removeYou()
   {
      this.setPiece(null);
      this.setBaseFieldsOwner(null);
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getType());
      return result.toString();
   }
}
