package cz.ismar.projects.IEdit.io.property;


public interface PropertyIOable
{

    public abstract void storeToProps(PropertyIO propertyio, String s);

    public abstract boolean loadFromProps(PropertyIO propertyio, String s);
}
