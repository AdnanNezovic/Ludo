package de.uniks.pmws2324.ludo.model;
import java.beans.PropertyChangeSupport;

public class Location
{
   public static final String PROPERTY_X = "x";
   public static final String PROPERTY_Y = "y";
   protected PropertyChangeSupport listeners;
   private double x;
   private double y;

   public double getX()
   {
      return this.x;
   }

   public Location setX(double value)
   {
      if (value == this.x)
      {
         return this;
      }

      final double oldValue = this.x;
      this.x = value;
      this.firePropertyChange(PROPERTY_X, oldValue, value);
      return this;
   }

   public double getY()
   {
      return this.y;
   }

   public Location setY(double value)
   {
      if (value == this.y)
      {
         return this;
      }

      final double oldValue = this.y;
      this.y = value;
      this.firePropertyChange(PROPERTY_Y, oldValue, value);
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
