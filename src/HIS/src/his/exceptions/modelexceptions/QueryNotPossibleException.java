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
package his.exceptions.modelexceptions;

/**
 *
 * @author silvio
 */
public class QueryNotPossibleException extends Exception {

    /**
     * Creates a new instance of <code>QueryNotPossibleException</code> without detail message.
     */
    public QueryNotPossibleException() {
    }

    /**
     * Constructs an instance of <code>QueryNotPossibleException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public QueryNotPossibleException(String msg) {
        super(msg);
    }
}
