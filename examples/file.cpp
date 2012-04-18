/**
 * * INAV - Interactive Network Active-traffic Visualization
 * * Copyright © 2007  Nathan Robinson, Jeff Scaparra
 * *
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


#include <fstream>
#include <iostream>
#include <string>


int main()
{
	std::ifstream file("stuff.txt");
	std::ios::iostate oldExceptions = file.exceptions();
	try
	{
		file.exceptions( std::ios::failbit | std::ios::badbit );
		std::string line;
		std::cout << "Reading file" << std::endl;

		while ( true )
		{
			std::getline( file, line );
			std::cout << line << std::endl;
		}
	}
	catch ( const std::ios_base::failure& error )
	{
		std::cerr << "I/O exception: " << error.what() << std::endl;
		return EXIT_FAILURE;
	}
	catch ( const std::exception& error )
	{
		std::cerr << "I/O exception: " << error.what() << std::endl;
		return EXIT_FAILURE;
	}
	catch ( ... )
	{
		std::cerr << "Unknown Exception" << std::endl;
		return EXIT_FAILURE;
	}

	file.close();


	return 0;
}
