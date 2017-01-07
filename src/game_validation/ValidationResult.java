package game_validation;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Alona on 1/7/2017.
 */
public class ValidationResult implements Iterable<String> {

    private final LinkedList<String> r_Messages;
    private boolean m_IsValid;

    public ValidationResult()
    {
        m_IsValid = true;
        r_Messages = new LinkedList<>();
    }

    @Override
    public Iterator<String> iterator()
    {
        return r_Messages.iterator();
    }

    public void add(String i_Message)
    {
        m_IsValid = false;
        r_Messages.add(i_Message);
    }

    public void add(ValidationResult i_ValidationResult)
    {
        m_IsValid = m_IsValid && i_ValidationResult.m_IsValid;
        r_Messages.addAll(i_ValidationResult.r_Messages);
    }

    public boolean isValid()
    {
        return m_IsValid;
    }
}
