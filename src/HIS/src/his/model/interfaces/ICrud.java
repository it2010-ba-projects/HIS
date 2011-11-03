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
package his.model.interfaces;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author silvio
 */
public interface ICrud<T> {
    abstract T findById(Serializable id);
    abstract Collection<? extends T> findAll();
    abstract void update(T t);
    abstract void delete(T t);
    abstract void create(T t);
}
