/**
 * * INAV - Interactive Network Active-traffic Visualization
 * * Copyright © 2007  Nathan Robinson, Jeff Scaparra
 * *
 * * This file is a part of INAV.
 * *
 * * This program is free software: you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation, either version 3 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * */


#ifndef VALUEEQUALS_H
#define VALUEEQUALS_H

#include <algorithm>

template <class K,class V>
class value_equals
{
	private:
		V value;
	public:
		//constructor (initialize value to compare with)
		value_equals( const V& v)
			: value(v) 
		{
		}

		bool operator() (std::pair<const K, V> elem)
		{
			return elem.second == value;
		}
};

template <class K, class V>
class value_equal_or_greater
{
	private:
		V value;
	public:
		//constructor (initialize value to compare with
		value_equal_or_greater( const V& v)
			:value(v)
		{
		}

		bool operator() (std::pair<const K, V> elem)
		{
			return elem.second >= value;
		}

};

template <class K, class V>
class value_equal_or_less
{
	private:
		V value;
	public:
		//constructor (initialize value to compare with
		value_equal_or_less( const V& v)
			:value(v)
		{
		}
		
		bool operator() (std::pair<const K, V> elem)
		{
			return elem.second <= value;
		}
};

#endif

