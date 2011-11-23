/*
    Copyright 2011 Silvio Wehner, Franziska Staake, Thomas Schulze
  
    This file is part of HIS.

    HIS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HIS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with HIS.  If not, see <http://www.gnu.org/licenses/>.
 
 */
package his.ui.validations;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextField;

/**
 * A class for performing basic validation on text fields. All it does is make 
 * sure that they are not null.
 * 
 * @author Michael Urban
 */
 
public class NotEmptyValidator extends AbstractValidator {
    public NotEmptyValidator(JDialog parent, JComponent c, String message) {
        super(parent, c, message);
    }
	
    protected boolean validationCriteria(JComponent c) {
        if (c instanceof JTextField) {
            if (((JTextField)c).getText().trim().isEmpty())
                return false;
        }
        else if (c instanceof JComboBox) {
            JComboBox cCombo = (JComboBox)c;
            if (cCombo.getSelectedItem() != null && 
                    cCombo.getSelectedItem().toString().trim().isEmpty())
                return false;
        }
        return true;
    }
}