package transaction.comm;

import java.io.Serializable;

// Message functions
public class Message implements MessageTypes, Serializable
{
    // Variable initialization
    int type;
    Object content;

    // Constructor
    public Message( int type, Object content )
    {
        this.type = type;
        this.content = content;
    }

    public Message( int type )
    {
        this( type, null );
    }

    // Getter method for type
    public int getType()
    {
        return type;
    }

    // Setter method for type
    public void setType( int type )
    {
        this.type = type;
    }

    // Getter method for message content
    public Object getContent()
    {
        return content;
    }

    // Setter method for message content
    public void setContent( Object content )
    {
        this.content = content;
    }
}
