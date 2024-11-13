package de.uniks.pmws2324.ludo.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Piece
extends Location {
   public static final String PROPERTY_COLOR = "color";
   public static final String PROPERTY_PIECE_FIELD = "pieceField";
   public static final String PROPERTY_PIECE_OWNER = "pieceOwner";
   private Color color;
   private Field pieceField;
   private Player pieceOwner;

   public Color getColor()
   {
      return this.color;
   }

   public Piece setColor(Color value)
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

   public Field getPieceField()
   {
      return this.pieceField;
   }

   public Piece setPieceField(Field value)
   {
      if (this.pieceField == value)
      {
         return this;
      }

      final Field oldValue = this.pieceField;
      if (this.pieceField != null)
      {
         this.pieceField = null;
         oldValue.setPiece(null);
      }
      this.pieceField = value;
      if (value != null)
      {
         value.setPiece(this);
      }
      this.firePropertyChange(PROPERTY_PIECE_FIELD, oldValue, value);
      return this;
   }

   public Player getPieceOwner()
   {
      return this.pieceOwner;
   }

   public Piece setPieceOwner(Player value)
   {
      if (this.pieceOwner == value)
      {
         return this;
      }

      final Player oldValue = this.pieceOwner;
      if (this.pieceOwner != null)
      {
         this.pieceOwner = null;
         oldValue.withoutPieces(this);
      }
      this.pieceOwner = value;
      if (value != null)
      {
         value.withPieces(this);
      }
      this.firePropertyChange(PROPERTY_PIECE_OWNER, oldValue, value);
      return this;
   }

   public void removeYou()
   {
      this.setPieceField(null);
      this.setPieceOwner(null);
   }
}
