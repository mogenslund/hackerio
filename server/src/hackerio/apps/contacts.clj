(ns hackerio.apps.contacts
  (:require [clojure.string :as str]))

;;; make delay of 3 sec on query
;;; GET / will return list of all ids and full names
;;; GET /<id> will return more data on the person 

;;; Should it implement some kind of handler?
;;; Or have something injected?


(def data
  [{:id 2002 :firstname "Andrew" :lastname "Carter" :birthdate "1932-12-22" :city "Saipan"}
   {:id 2007 :firstname "Thomas" :lastname "Lee" :birthdate "1988-05-21" :city "Monaco"}
   {:id 2009 :firstname "Samantha" :lastname "Jackson" :birthdate "1992-11-04" :city "Kiev"}
   {:id 2011 :firstname "Elizabeth" :lastname "Nelson" :birthdate "1949-01-13" :city "Manila"}
   {:id 2019 :firstname "Kristen" :lastname "Gutierrez" :birthdate "2001-02-09" :city "Hanoi"}
   {:id 2023 :firstname "Megan" :lastname "Diaz" :birthdate "1994-09-13" :city "Riga"}
   {:id 2029 :firstname "Christina" :lastname "Hill" :birthdate "2004-10-13" :city "Mexico City"}
   {:id 2037 :firstname "Krystal" :lastname "Jackson" :birthdate "1987-05-11" :city "Thimphu"}
   {:id 2042 :firstname "Kyle" :lastname "Williams" :birthdate "1935-04-11" :city "Damascus"}
   {:id 2046 :firstname "Sara" :lastname "Walker" :birthdate "1936-09-23" :city "Vientiane"}
   {:id 2047 :firstname "Erica" :lastname "Cook" :birthdate "1943-12-23" :city "Paris"}
   {:id 2053 :firstname "David" :lastname "Turner" :birthdate "1960-12-02" :city "Stanley"}
   {:id 2054 :firstname "Tara" :lastname "Morgan" :birthdate "1961-10-14" :city "Kiev"}
   {:id 2061 :firstname "Courtney" :lastname "Scott" :birthdate "2005-10-06" :city "Pristina"}
   {:id 2068 :firstname "Andrea" :lastname "Lewis" :birthdate "1988-01-25" :city "Damascus"}
   {:id 2069 :firstname "Sean" :lastname "Cooper" :birthdate "1935-12-06" :city "Hanoi"}
   {:id 2078 :firstname "Megan" :lastname "Anderson" :birthdate "1987-01-12" :city "Hamilton"}
   {:id 2086 :firstname "Jenna" :lastname "Brown" :birthdate "1930-05-16" :city "Maseru"}
   {:id 2089 :firstname "Christine" :lastname "Hall" :birthdate "1947-07-12" :city "Kabul"}
   {:id 2096 :firstname "Edward" :lastname "Miller" :birthdate "1935-05-21" :city "Maseru"}
   {:id 2099 :firstname "Emily" :lastname "Cook" :birthdate "1983-02-19" :city "Pyongyang"}
   {:id 2107 :firstname "Lindsey" :lastname "Robinson" :birthdate "1985-10-01" :city "Taipei"}
   {:id 2117 :firstname "Kristin" :lastname "Peterson" :birthdate "1976-03-21" :city "Oslo"}
   {:id 2122 :firstname "Lindsay" :lastname "Sanchez" :birthdate "1954-06-26" :city "Maputo"}
   {:id 2131 :firstname "Juan" :lastname "Brown" :birthdate "1980-10-21" :city "Damascus"}
   {:id 2136 :firstname "Jenna" :lastname "Hill" :birthdate "1958-01-16" :city "Muscat"}
   {:id 2138 :firstname "Lauren" :lastname "Allen" :birthdate "1931-04-27" :city "Tehran"}
   {:id 2145 :firstname "Shane" :lastname "Peterson" :birthdate "1990-03-15" :city "Willemstad"}
   {:id 2154 :firstname "Amy" :lastname "Mitchell" :birthdate "1997-07-22" :city "Dhaka"}
   {:id 2162 :firstname "Tiffany" :lastname "Morgan" :birthdate "1976-04-19" :city "Kiev"}
   {:id 2163 :firstname "Katie" :lastname "Wilson" :birthdate "1960-10-22" :city "Jakarta"}
   {:id 2164 :firstname "Kristina" :lastname "Johnson" :birthdate "2001-04-08" :city "Vientiane"}
   {:id 2165 :firstname "Katherine" :lastname "King" :birthdate "1948-07-02" :city "Oslo"}
   {:id 2173 :firstname "Lindsey" :lastname "Martinez" :birthdate "2001-10-03" :city "Manila"}
   {:id 2180 :firstname "Jesse" :lastname "Walker" :birthdate "1932-04-16" :city "Hamilton"}
   {:id 2186 :firstname "Jennifer" :lastname "Johnson" :birthdate "1966-08-07" :city "Hanoi"}
   {:id 2193 :firstname "Benjamin" :lastname "Peterson" :birthdate "1929-05-25" :city "Helsinki"}
   {:id 2202 :firstname "Stephanie" :lastname "Morgan" :birthdate "1932-02-28" :city "Warsaw"}
   {:id 2206 :firstname "Jack" :lastname "Ripper" :birthdate "1966-12-11" :city "London"}
   {:id 2207 :firstname "Steven" :lastname "Smith" :birthdate "1969-01-20" :city "Kingston"}
   {:id 2216 :firstname "Allison" :lastname "Carter" :birthdate "1966-11-12" :city "Luxembourg"}
   {:id 2226 :firstname "Sean" :lastname "Roberts" :birthdate "1974-09-01" :city "Tirana"}
   {:id 2227 :firstname "Sarah" :lastname "Evans" :birthdate "1942-04-25" :city "Tegucigalpa"}
   {:id 2233 :firstname "Erica" :lastname "Rodriguez" :birthdate "1948-04-14" :city "Dublin"}
   {:id 2243 :firstname "Jordan" :lastname "Walker" :birthdate "2005-03-07" :city "Minsk"}
   {:id 2250 :firstname "Jacqueline" :lastname "Torres" :birthdate "1952-12-19" :city "Reykjavík"}
   {:id 2255 :firstname "Samantha" :lastname "Evans" :birthdate "1995-04-22" :city "Minsk"}
   {:id 2262 :firstname "Bryan" :lastname "Rogers" :birthdate "1981-08-23" :city "Nicosia"}
   {:id 2271 :firstname "Joshua" :lastname "Smith" :birthdate "1931-02-13" :city "Jamestown"}
   {:id 2273 :firstname "Richard" :lastname "Hall" :birthdate "1933-10-01" :city "Tegucigalpa"}
   {:id 2282 :firstname "Emily" :lastname "Hall" :birthdate "1979-10-08" :city "Tunis"}
   {:id 2286 :firstname "Lisa" :lastname "Nguyen" :birthdate "1953-10-06" :city "Stockholm"}
   {:id 2296 :firstname "Kimberly" :lastname "Perez" :birthdate "1956-05-23" :city "Prague"}
   {:id 2304 :firstname "Monica" :lastname "Wilson" :birthdate "1988-12-27" :city "Dhaka"}
   {:id 2305 :firstname "Maria" :lastname "Reyes" :birthdate "1937-10-21" :city "Tokyo"}
   {:id 2309 :firstname "Megan" :lastname "Garcia" :birthdate "1992-04-18" :city "Copenhagen"}
   {:id 2319 :firstname "Benjamin" :lastname "Jones" :birthdate "1971-05-15" :city "Nicosia"}
   {:id 2323 :firstname "Scott" :lastname "Murphy" :birthdate "1961-08-03" :city "Pristina"}
   {:id 2327 :firstname "Emily" :lastname "Ramirez" :birthdate "1942-08-02" :city "Kingstown"}
   {:id 2332 :firstname "Melissa" :lastname "Scott" :birthdate "1939-08-01" :city "Tehran"}
   {:id 2341 :firstname "Joshua" :lastname "Perez" :birthdate "1930-09-20" :city "Castries"}
   {:id 2351 :firstname "Sarah" :lastname "Murphy" :birthdate "1969-03-03" :city "Moscow"}
   {:id 2360 :firstname "Joel" :lastname "Robinson" :birthdate "1980-07-19" :city "Tunis"}
   {:id 2361 :firstname "Joseph" :lastname "Sanchez" :birthdate "1994-02-10" :city "Stanley"}
   {:id 2363 :firstname "Corey" :lastname "Sanchez" :birthdate "1934-06-05" :city "Philipsburg"}
   {:id 2368 :firstname "Christina" :lastname "Jackson" :birthdate "1955-08-06" :city "Tegucigalpa"}
   {:id 2375 :firstname "Angela" :lastname "Gutierrez" :birthdate "1957-05-10" :city "Stockholm"}
   {:id 2385 :firstname "Krystal" :lastname "Rodriguez" :birthdate "1955-10-01" :city "Helsinki"}
   {:id 2390 :firstname "Melissa" :lastname "Rodriguez" :birthdate "1988-07-04" :city "Kampala"}
   {:id 2400 :firstname "Stephen" :lastname "Thompson" :birthdate "1944-10-23" :city "Canberra"}
   {:id 2409 :firstname "Jacob" :lastname "Clark" :birthdate "1942-09-04" :city "Castries"}
   {:id 2417 :firstname "Megan" :lastname "Ortiz" :birthdate "1953-06-21" :city "Valletta"}
   {:id 2420 :firstname "Edward" :lastname "Gutierrez" :birthdate "1937-08-02" :city "Kiev"}
   {:id 2428 :firstname "Elizabeth" :lastname "Williams" :birthdate "1960-03-10" :city "Seoul"}
   {:id 2436 :firstname "Joshua" :lastname "White" :birthdate "1964-03-07" :city "Nicosia"}
   {:id 2442 :firstname "Courtney" :lastname "Perez" :birthdate "1937-12-22" :city "Stockholm"}
   {:id 2443 :firstname "Peter" :lastname "Mitchell" :birthdate "2005-07-13" :city "Kuwait City"}
   {:id 2450 :firstname "Alicia" :lastname "Roberts" :birthdate "1938-11-24" :city "Juba"}
   {:id 2456 :firstname "Jeffrey" :lastname "Campbell" :birthdate "1996-01-12" :city "Riyadh"}
   {:id 2460 :firstname "John" :lastname "Anderson" :birthdate "1940-06-07" :city "Victoria"}
   {:id 2470 :firstname "Jeremy" :lastname "Taylor" :birthdate "1981-07-13" :city "Copenhagen"}
   {:id 2479 :firstname "Shawn" :lastname "Lopez" :birthdate "1982-10-20" :city "Manila"}
   {:id 2480 :firstname "Keith" :lastname "Torres" :birthdate "1993-07-22" :city "Niamey"}
   {:id 2483 :firstname "Derek" :lastname "Allen" :birthdate "1971-12-15" :city "Muscat"}
   {:id 2493 :firstname "Brittany" :lastname "Robinson" :birthdate "1933-07-18" :city "Castries"}
   {:id 2501 :firstname "Matthew" :lastname "Adams" :birthdate "1955-01-28" :city "Ottawa"}
   {:id 2510 :firstname "Maria" :lastname "Collins" :birthdate "1980-08-25" :city "Tallinn"}
   {:id 2519 :firstname "Mary" :lastname "Jones" :birthdate "1940-04-10" :city "Sarajevo"}
   {:id 2528 :firstname "April" :lastname "Young" :birthdate "1940-11-05" :city "Dublin"}
   {:id 2534 :firstname "Cody" :lastname "Harris" :birthdate "1998-12-18" :city "Pyongyang"}
   {:id 2541 :firstname "Christine" :lastname "Hernandez" :birthdate "1996-08-09" :city "Sarajevo"}
   {:id 2550 :firstname "Jonathan" :lastname "Ramirez" :birthdate "1926-05-20" :city "Kinshasa"}
   {:id 2558 :firstname "Thomas" :lastname "Morgan" :birthdate "1946-02-07" :city "Dublin"}
   {:id 2559 :firstname "Brittany" :lastname "Evans" :birthdate "1959-03-11" :city "Madrid"}
   {:id 2565 :firstname "John" :lastname "Murphy" :birthdate "1990-06-10" :city "Jakarta"}
   {:id 2572 :firstname "Donald" :lastname "Smith" :birthdate "2000-10-28" :city "Lima"}
   {:id 2579 :firstname "Bryan" :lastname "Gonzalez" :birthdate "1975-09-05" :city "Castries"}
   {:id 2585 :firstname "Tyler" :lastname "Walker" :birthdate "1982-04-15" :city "Hanoi"}
   {:id 2587 :firstname "Bradley" :lastname "Martinez" :birthdate "1941-07-25" :city "Riga"}
   {:id 2591 :firstname "Richard" :lastname "Roberts" :birthdate "1965-03-18" :city "Skopje"}
   {:id 2593 :firstname "William" :lastname "Lopez" :birthdate "1931-01-18" :city "Washington"}
   {:id 2600 :firstname "Juan" :lastname "Brown" :birthdate "1980-09-23" :city "Stockholm"}
   {:id 2609 :firstname "Chad" :lastname "Robinson" :birthdate "1954-01-18" :city "Washington"}
   {:id 2611 :firstname "Ryan" :lastname "Harris" :birthdate "2005-01-22" :city "Minsk"}
   {:id 2616 :firstname "Kristen" :lastname "Cooper" :birthdate "1984-07-12" :city "Vientiane"}
   {:id 2625 :firstname "Tara" :lastname "Allen" :birthdate "1960-12-21" :city "Wellington"}
   {:id 2633 :firstname "Ryan" :lastname "Wright" :birthdate "1939-01-03" :city "Nairobi"}
   {:id 2634 :firstname "Dustin" :lastname "Young" :birthdate "1992-09-25" :city "Niamey"}
   {:id 2639 :firstname "Daniel" :lastname "Ortiz" :birthdate "1952-06-24" :city "Madrid"}
   {:id 2648 :firstname "Anthony" :lastname "Nelson" :birthdate "1968-10-14" :city "Seoul"}
   {:id 2650 :firstname "Ashley" :lastname "Thompson" :birthdate "1956-09-18" :city "Riyadh"}
   {:id 2651 :firstname "Thomas" :lastname "Robinson" :birthdate "1936-12-16" :city "Kinshasa"}
   {:id 2655 :firstname "Brandon" :lastname "Davis" :birthdate "1955-08-22" :city "Charlotte Amalie"}
   {:id 2659 :firstname "Heather" :lastname "Smith" :birthdate "1949-08-08" :city "Vientiane"}
   {:id 2661 :firstname "Melissa" :lastname "Sanchez" :birthdate "1948-05-28" :city "Luxembourg"}
   {:id 2668 :firstname "Christina" :lastname "Ortiz" :birthdate "1986-01-08" :city "Hamilton"}
   {:id 2677 :firstname "Joseph" :lastname "Johnson" :birthdate "1983-04-27" :city "Riga"}
   {:id 2685 :firstname "Michael" :lastname "Smith" :birthdate "1993-04-01" :city "Dhaka"}
   {:id 2687 :firstname "Edward" :lastname "Evans" :birthdate "2004-05-23" :city "Kabul"}
   {:id 2688 :firstname "Sarah" :lastname "Lopez" :birthdate "1945-11-22" :city "Ottawa"}
   {:id 2692 :firstname "Alexander" :lastname "Gomez" :birthdate "1942-03-18" :city "Saipan"}
   {:id 2695 :firstname "Emily" :lastname "Hall" :birthdate "1936-01-27" :city "Paris"}
   {:id 2697 :firstname "Christine" :lastname "Torres" :birthdate "2001-11-02" :city "Washington"}
   {:id 2706 :firstname "Angela" :lastname "Williams" :birthdate "1951-06-08" :city "Kiev"}
   {:id 2712 :firstname "Laura" :lastname "Williams" :birthdate "1954-10-10" :city "Nairobi"}
   {:id 2713 :firstname "Bradley" :lastname "Hill" :birthdate "1970-04-18" :city "Kabul"}
   {:id 2723 :firstname "Katherine" :lastname "Morris" :birthdate "1926-04-26" :city "Nicosia"}
   {:id 2724 :firstname "Alicia" :lastname "Collins" :birthdate "1943-10-21" :city "Maseru"}
   {:id 2730 :firstname "Kelly" :lastname "Gonzalez" :birthdate "1980-02-07" :city "Riga"}
   {:id 2731 :firstname "Jose" :lastname "Nelson" :birthdate "1927-08-06" :city "Wellington"}
   {:id 2741 :firstname "Jacqueline" :lastname "Nguyen" :birthdate "1973-05-22" :city "Vienna"}
   {:id 2745 :firstname "Katie" :lastname "Martinez" :birthdate "1968-07-22" :city "Praia"}
   {:id 2750 :firstname "Jordan" :lastname "Campbell" :birthdate "1960-12-24" :city "Copenhagen"}
   {:id 2758 :firstname "Jeremy" :lastname "Stewart" :birthdate "1947-03-11" :city "Rome"}
   {:id 2763 :firstname "Tara" :lastname "Reyes" :birthdate "1976-08-06" :city "Praia"}
   {:id 2769 :firstname "Kristen" :lastname "Davis" :birthdate "1983-09-26" :city "Havana"}
   {:id 2776 :firstname "Natalie" :lastname "Thompson" :birthdate "1976-08-10" :city "Castries"}
   {:id 2785 :firstname "Tyler" :lastname "Young" :birthdate "1958-11-13" :city "Dublin"}
   {:id 2786 :firstname "Travis" :lastname "Nelson" :birthdate "1949-10-28" :city "Lisbon"}
   {:id 2789 :firstname "Robert" :lastname "Morris" :birthdate "1982-08-24" :city "Monaco"}
   {:id 2792 :firstname "Crystal" :lastname "Jones" :birthdate "1945-08-25" :city "Juba"}
   {:id 2793 :firstname "Angela" :lastname "Ramirez" :birthdate "1933-04-12" :city "Warsaw"}
   {:id 2800 :firstname "Steven" :lastname "Parker" :birthdate "1986-09-13" :city "Kampala"}
   {:id 2805 :firstname "Jose" :lastname "Roberts" :birthdate "1988-11-13" :city "Oslo"}
   {:id 2810 :firstname "Gregory" :lastname "Campbell" :birthdate "1926-05-23" :city "Willemstad"}
   {:id 2820 :firstname "Jenna" :lastname "Roberts" :birthdate "1971-06-05" :city "Dhaka"}
   {:id 2825 :firstname "Emily" :lastname "Nelson" :birthdate "1932-03-10" :city "Luxembourg"}
   {:id 2832 :firstname "Samantha" :lastname "Davis" :birthdate "1946-11-22" :city "Ankara"}
   {:id 2839 :firstname "Kenneth" :lastname "Sanchez" :birthdate "1967-05-27" :city "Gibraltar"}
   {:id 2848 :firstname "Heather" :lastname "Wright" :birthdate "1946-08-06" :city "Jamestown"}
   {:id 2853 :firstname "Jose" :lastname "Edwards" :birthdate "1932-12-20" :city "Vientiane"}
   {:id 2856 :firstname "Tyler" :lastname "Jackson" :birthdate "1983-05-22" :city "Washington"}
   {:id 2859 :firstname "Erin" :lastname "Wilson" :birthdate "1984-05-09" :city "Tegucigalpa"}
   {:id 2860 :firstname "Krystal" :lastname "Stewart" :birthdate "1969-04-24" :city "Hamilton"}
   {:id 2870 :firstname "Kristina" :lastname "Collins" :birthdate "1998-03-14" :city "Ottawa"}
   {:id 2876 :firstname "Timothy" :lastname "Reyes" :birthdate "1982-06-21" :city "Paris"}
   {:id 2878 :firstname "Kevin" :lastname "Wilson" :birthdate "1991-09-05" :city "Kinshasa"}
   {:id 2887 :firstname "Lauren" :lastname "Johnson" :birthdate "1940-10-12" :city "Valletta"}
   {:id 2892 :firstname "Steven" :lastname "Hill" :birthdate "1948-01-13" :city "Praia"}
   {:id 2895 :firstname "Alicia" :lastname "Martinez" :birthdate "1974-08-06" :city "Oslo"}
   {:id 2901 :firstname "Natalie" :lastname "Williams" :birthdate "1965-06-05" :city "Copenhagen"}
   {:id 2902 :firstname "Steven" :lastname "Phillips" :birthdate "1994-06-10" :city "Jamestown"}
   {:id 2904 :firstname "Alicia" :lastname "Campbell" :birthdate "1997-11-25" :city "Tunis"}
   {:id 2913 :firstname "Alicia" :lastname "Jones" :birthdate "1945-05-07" :city "Nassau"}
   {:id 2920 :firstname "Alexander" :lastname "Torres" :birthdate "1954-10-23" :city "Charlotte Amalie"}
   {:id 2928 :firstname "Laura" :lastname "Garcia" :birthdate "1932-06-02" :city "Dublin"}
   {:id 2937 :firstname "Daniel" :lastname "Perez" :birthdate "1976-05-22" :city "Tashkent"}
   {:id 2946 :firstname "Andrew" :lastname "Clark" :birthdate "1961-04-11" :city "Castries"}
   {:id 2949 :firstname "Ashley" :lastname "Johnson" :birthdate "1934-12-12" :city "Damascus"}
   {:id 2955 :firstname "Juan" :lastname "Flores" :birthdate "1980-08-16" :city "Pyongyang"}
   {:id 2964 :firstname "David" :lastname "Nilson" :birthdate "1929-09-16" :city "Kuwait City"}
   {:id 2973 :firstname "Jacqueline" :lastname "Nguyen" :birthdate "1933-06-14" :city "Kathmandu"}
   {:id 2977 :firstname "Andrea" :lastname "Taylor" :birthdate "1970-06-16" :city "Praia"}
   {:id 2985 :firstname "Rachel" :lastname "Parker" :birthdate "1931-08-13" :city "Maseru"}
   {:id 2993 :firstname "Rachel" :lastname "Sanchez" :birthdate "2003-06-16" :city "Canberra"}
   {:id 3000 :firstname "Anthony" :lastname "Johnson" :birthdate "1985-11-14" :city "Kampala"}
   {:id 3009 :firstname "Patrick" :lastname "Taylor" :birthdate "1985-06-28" :city "Valletta"}
   {:id 3016 :firstname "Jessica" :lastname "Clark" :birthdate "1947-11-12" :city "Gibraltar"}
   {:id 3022 :firstname "Elizabeth" :lastname "Adams" :birthdate "2000-10-01" :city "Manila"}
   {:id 3030 :firstname "Kyle" :lastname "Lee" :birthdate "1970-07-09" :city "Moscow"}
   {:id 3038 :firstname "Anthony" :lastname "Moore" :birthdate "1931-06-18" :city "Doha"}
   {:id 3041 :firstname "Lisa" :lastname "Flores" :birthdate "1981-02-21" :city "Oslo"}
   {:id 3046 :firstname "Jason" :lastname "Wright" :birthdate "2001-03-12" :city "Tegucigalpa"}
   {:id 3047 :firstname "Jamie" :lastname "King" :birthdate "1933-09-08" :city "Luxembourg"}
   {:id 3048 :firstname "Michael" :lastname "Reyes" :birthdate "1931-01-20" :city "Mogadishu"}
   {:id 3054 :firstname "Marcus" :lastname "Turner" :birthdate "1985-07-08" :city "Hanoi"}
   {:id 3058 :firstname "Daniel" :lastname "Diaz" :birthdate "1992-10-27" :city "Charlotte Amalie"}
   {:id 3063 :firstname "Lisa" :lastname "Lee" :birthdate "1954-04-18" :city "Gibraltar"}
   {:id 3068 :firstname "Keith" :lastname "Smith" :birthdate "1955-05-18" :city "Kinshasa"}
   {:id 3071 :firstname "Bradley" :lastname "Perez" :birthdate "1936-03-02" :city "Warsaw"}
   {:id 3078 :firstname "Sara" :lastname "Peterson" :birthdate "1990-02-02" :city "Stockholm"}
   {:id 3083 :firstname "Benjamin" :lastname "Scott" :birthdate "1929-12-05" :city "Copenhagen"}
   {:id 3090 :firstname "David" :lastname "Cooper" :birthdate "1939-10-03" :city "Prague"}
   {:id 3095 :firstname "Patrick" :lastname "Stewart" :birthdate "1976-05-20" :city "Moscow"}
   {:id 3104 :firstname "Joel" :lastname "Lopez" :birthdate "1952-01-03" :city "Stockholm"}
   {:id 3114 :firstname "Jenna" :lastname "Taylor" :birthdate "1990-12-25" :city "Valletta"}
   {:id 3117 :firstname "Vanessa" :lastname "Reyes" :birthdate "1991-09-13" :city "Havana"}
   {:id 3121 :firstname "Joshua" :lastname "Taylor" :birthdate "1958-01-01" :city "Jakarta"}
   {:id 3131 :firstname "Allison" :lastname "Morgan" :birthdate "1952-12-24" :city "Helsinki"}
   {:id 3138 :firstname "Sara" :lastname "Harris" :birthdate "1943-04-16" :city "Lima"}
   {:id 3139 :firstname "Amy" :lastname "Jackson" :birthdate "1988-09-20" :city "Dhaka"}
   {:id 3143 :firstname "Jennifer" :lastname "Perez" :birthdate "1983-07-28" :city "Kathmandu"}
   {:id 3144 :firstname "Melissa" :lastname "Cruz" :birthdate "1959-01-14" :city "Kuwait City"}
   {:id 3150 :firstname "Brian" :lastname "Cruz" :birthdate "1997-07-10" :city "Singapore"}
   {:id 3160 :firstname "Kristina" :lastname "Lee" :birthdate "2002-12-12" :city "Tallinn"}
   {:id 3169 :firstname "Jesse" :lastname "Young" :birthdate "1978-02-12" :city "Copenhagen"}
   {:id 3171 :firstname "Chad" :lastname "Anderson" :birthdate "1967-02-25" :city "Paris"}
   {:id 3181 :firstname "Marcus" :lastname "Morales" :birthdate "1978-10-27" :city "Kingston"}
   {:id 3191 :firstname "Cody" :lastname "Perez" :birthdate "1940-07-04" :city "Taipei"}
   {:id 3201 :firstname "Brian" :lastname "Torres" :birthdate "1939-11-23" :city "Willemstad"}
   {:id 3202 :firstname "Corey" :lastname "Ramirez" :birthdate "1975-05-17" :city "Nairobi"}
   {:id 3209 :firstname "Stephanie" :lastname "Gomez" :birthdate "1940-09-12" :city "Doha"}
   {:id 3215 :firstname "Anna" :lastname "Hill" :birthdate "1965-01-18" :city "Warsaw"}
   {:id 3219 :firstname "Erica" :lastname "Sanchez" :birthdate "1928-07-16" :city "Hanga Roa"}
   {:id 3226 :firstname "Andrew" :lastname "Moore" :birthdate "1936-11-08" :city "Montevideo"}
   {:id 3233 :firstname "Paul" :lastname "Green" :birthdate "1981-08-08" :city "Sofia"}
   {:id 3240 :firstname "Nicholas" :lastname "Robinson" :birthdate "1996-06-23" :city "Kigali"}
   {:id 3243 :firstname "John" :lastname "Gonzalez" :birthdate "1991-05-21" :city "Ottawa"}
   {:id 3248 :firstname "Matthew" :lastname "Scott" :birthdate "1970-02-11" :city "Mogadishu"}
   {:id 3249 :firstname "Anna" :lastname "Thompson" :birthdate "1952-10-28" :city "Tokyo"}
   {:id 3252 :firstname "Heather" :lastname "Mitchell" :birthdate "1935-01-17" :city "Taipei"}
   {:id 3259 :firstname "Lindsay" :lastname "Roberts" :birthdate "1993-09-11" :city "Thimphu"}
   {:id 3269 :firstname "Holly" :lastname "Allen" :birthdate "1961-12-12" :city "Damascus"}
   {:id 3275 :firstname "Angela" :lastname "Johnson" :birthdate "1999-01-24" :city "Vienna"}
   {:id 3277 :firstname "Shawn" :lastname "Taylor" :birthdate "1986-10-14" :city "Juba"}
   {:id 3285 :firstname "Amanda" :lastname "Sanchez" :birthdate "1946-01-06" :city "Thimphu"}
   {:id 3295 :firstname "Heather" :lastname "Moore" :birthdate "1932-10-25" :city "Road Town"}
   {:id 3300 :firstname "Mary" :lastname "Carter" :birthdate "1958-08-12" :city "Tunis"}
   {:id 3303 :firstname "Sean" :lastname "Cooper" :birthdate "1939-09-24" :city "Pyongyang"}
   {:id 3305 :firstname "Elizabeth" :lastname "Roberts" :birthdate "1936-08-04" :city "Willemstad"}
   {:id 3314 :firstname "Joshua" :lastname "Peterson" :birthdate "1962-04-26" :city "Hanga Roa"}
   {:id 3321 :firstname "Lauren" :lastname "Perez" :birthdate "2004-04-03" :city "Tashkent"}
   {:id 3325 :firstname "Dustin" :lastname "Morgan" :birthdate "1960-08-10" :city "Kingston"}
   {:id 3333 :firstname "Jared" :lastname "White" :birthdate "1932-12-12" :city "Lima"}
   {:id 3340 :firstname "Jacob" :lastname "Diaz" :birthdate "2003-03-11" :city "Islamabad"}
   {:id 3345 :firstname "Cody" :lastname "Thompson" :birthdate "1961-10-06" :city "Rome"}
   {:id 3352 :firstname "Peter" :lastname "Williams" :birthdate "1985-07-17" :city "Tunis"}
   {:id 3354 :firstname "Timothy" :lastname "King" :birthdate "1973-03-08" :city "Washington"}
   {:id 3362 :firstname "Joshua" :lastname "Moore" :birthdate "1976-03-28" :city "Havana"}
   {:id 3363 :firstname "Jose" :lastname "Rodriguez" :birthdate "1951-11-21" :city "Sarajevo"}
   {:id 3365 :firstname "Victoria" :lastname "Torres" :birthdate "1928-05-03" :city "Tirana"}
   {:id 3371 :firstname "Brittany" :lastname "Morris" :birthdate "1947-06-09" :city "Nassau"}
   {:id 3375 :firstname "Rebecca" :lastname "Evans" :birthdate "1971-07-28" :city "Pyongyang"}
   {:id 3384 :firstname "Juan" :lastname "Garcia" :birthdate "2000-10-19" :city "Kinshasa"}
   {:id 3391 :firstname "Matthew" :lastname "Gutierrez" :birthdate "1995-03-05" :city "Nicosia"}
   {:id 3400 :firstname "Marcus" :lastname "Smith" :birthdate "1984-04-16" :city "Dakar"}
   {:id 3406 :firstname "Brittany" :lastname "Lee" :birthdate "2002-02-20" :city "Tegucigalpa"}
   {:id 3416 :firstname "Kimberly" :lastname "Mitchell" :birthdate "1931-08-04" :city "Castries"}
   {:id 3418 :firstname "Steven" :lastname "Cruz" :birthdate "1967-02-08" :city "Hanoi"}
   {:id 3419 :firstname "Lindsay" :lastname "Persson" :birthdate "1992-09-07" :city "Mexico City"}
   {:id 3427 :firstname "Jacob" :lastname "Rogers" :birthdate "1935-02-28" :city "Nicosia"}
   {:id 3431 :firstname "David" :lastname "Morris" :birthdate "1931-03-27" :city "Maseru"}
   {:id 3432 :firstname "Brittany" :lastname "Peterson" :birthdate "1986-09-03" :city "Praia"}
   {:id 3434 :firstname "Sergio" :lastname "Molina" :birthdate "1976-11-01" :city "Mexico City"}
   {:id 3437 :firstname "Donald" :lastname "Smith" :birthdate "1971-01-15" :city "Moscow"}
   {:id 3442 :firstname "Vanessa" :lastname "Diaz" :birthdate "1980-04-10" :city "Hanoi"}
   {:id 3449 :firstname "Derek" :lastname "Collins" :birthdate "1987-10-20" :city "Kingston"}
   {:id 3450 :firstname "Jonathan" :lastname "Gomez" :birthdate "1949-03-08" :city "Caracas"}
   {:id 3457 :firstname "Julie" :lastname "Rivera" :birthdate "1926-03-09" :city "Pristina"}
   {:id 3458 :firstname "Melissa" :lastname "Martin" :birthdate "1970-10-19" :city "Tegucigalpa"}
   {:id 3462 :firstname "Steven" :lastname "Rodriguez" :birthdate "1930-12-23" :city "Oslo"}
   {:id 3470 :firstname "Jordan" :lastname "Murphy" :birthdate "1939-11-24" :city "Caracas"}
   {:id 3475 :firstname "Timothy" :lastname "Mitchell" :birthdate "1936-04-03" :city "Reykjavík"}
   {:id 3481 :firstname "Jenna" :lastname "Harris" :birthdate "1939-11-18" :city "Mogadishu"}
   {:id 3491 :firstname "Jessica" :lastname "Cook" :birthdate "1970-10-11" :city "Nicosia"}
   {:id 3501 :firstname "William" :lastname "Morris" :birthdate "2000-10-22" :city "Lima"}
   {:id 3507 :firstname "Stephanie" :lastname "Phillips" :birthdate "1972-01-21" :city "Singapore"}
   {:id 3511 :firstname "Matthew" :lastname "Garcia" :birthdate "1996-06-09" :city "Kabul"}
   {:id 3519 :firstname "Shannon" :lastname "Lee" :birthdate "1983-09-15" :city "Valletta"}
   {:id 3527 :firstname "Stephen" :lastname "Murphy" :birthdate "1978-06-11" :city "Riga"}
   {:id 3537 :firstname "Matthew" :lastname "Lopez" :birthdate "2005-04-24" :city "Seoul"}
   {:id 3540 :firstname "Lauren" :lastname "Gonzalez" :birthdate "1989-10-26" :city "Copenhagen"}
   {:id 3542 :firstname "Samantha" :lastname "Williams" :birthdate "2002-09-19" :city "Mexico City"}
   {:id 3547 :firstname "Mary" :lastname "Brown" :birthdate "1994-05-16" :city "Valletta"}
   {:id 3556 :firstname "Laura" :lastname "Nelson" :birthdate "1955-06-22" :city "Dhaka"}
   {:id 3564 :firstname "Tyler" :lastname "Harris" :birthdate "1985-09-17" :city "Maputo"}
   {:id 3570 :firstname "Kelly" :lastname "Turner" :birthdate "1949-07-08" :city "Monaco"}
   {:id 3576 :firstname "Zachary" :lastname "Collins" :birthdate "2005-12-07" :city "Kigali"}
   {:id 3582 :firstname "Tara" :lastname "Rodriguez" :birthdate "1933-03-18" :city "Montevideo"}
   {:id 3588 :firstname "Jose" :lastname "Edwards" :birthdate "1980-09-12" :city "Kingston"}
   {:id 3591 :firstname "Jason" :lastname "Davis" :birthdate "1971-08-28" :city "Taipei"}
   {:id 3593 :firstname "Meghan" :lastname "Campbell" :birthdate "1979-08-10" :city "Tarawa"}
   {:id 3596 :firstname "Daniel" :lastname "Allen" :birthdate "1984-06-01" :city "Kigali"}
   {:id 3604 :firstname "Joshua" :lastname "Perez" :birthdate "1963-07-09" :city "Saipan"}
   {:id 3606 :firstname "Lauren" :lastname "Evans" :birthdate "1937-05-06" :city "Paris"}
   {:id 3611 :firstname "April" :lastname "Diaz" :birthdate "1934-09-02" :city "Sarajevo"}
   {:id 3619 :firstname "Joseph" :lastname "Cruz" :birthdate "1990-11-28" :city "Victoria"}
   {:id 3627 :firstname "Kimberly" :lastname "Davis" :birthdate "1978-11-20" :city "Saipan"}
   {:id 3629 :firstname "Kyle" :lastname "Diaz" :birthdate "1935-01-27" :city "Chisinau"}
   {:id 3631 :firstname "Juan" :lastname "Ortiz" :birthdate "1955-03-12" :city "Lima"}
   {:id 3640 :firstname "Paul" :lastname "Cruz" :birthdate "1975-12-28" :city "Philipsburg"}
   {:id 3647 :firstname "Thomas" :lastname "Campbell" :birthdate "1991-11-21" :city "Caracas"}
   {:id 3648 :firstname "Jenna" :lastname "Green" :birthdate "1977-11-19" :city "Madrid"}
   {:id 3652 :firstname "Jordan" :lastname "Turner" :birthdate "1955-08-02" :city "Tashkent"}
   {:id 3659 :firstname "Lauren" :lastname "Young" :birthdate "1939-11-01" :city "Gibraltar"}
   {:id 3668 :firstname "Laura" :lastname "Reyes" :birthdate "2003-02-24" :city "Sarajevo"}
   {:id 3669 :firstname "Benjamin" :lastname "Evans" :birthdate "1988-06-02" :city "Mexico City"}
   {:id 3674 :firstname "Jenna" :lastname "Parker" :birthdate "1965-04-27" :city "Paris"}
   {:id 3677 :firstname "Lisa" :lastname "Carter" :birthdate "1987-10-19" :city "Paris"}
   {:id 3678 :firstname "Ryan" :lastname "Hernandez" :birthdate "1953-12-12" :city "Dakar"}
   {:id 3687 :firstname "Robert" :lastname "Murphy" :birthdate "1977-05-09" :city "Minsk"}
   {:id 3692 :firstname "Crystal" :lastname "Turner" :birthdate "1929-07-22" :city "Nicosia"}
   {:id 3693 :firstname "Kyle" :lastname "Brown" :birthdate "1962-04-10" :city "Pristina"}
   {:id 3700 :firstname "Erica" :lastname "Cook" :birthdate "1959-09-04" :city "Philipsburg"}
   {:id 3708 :firstname "Julie" :lastname "Parker" :birthdate "1985-09-24" :city "Praia"}
   {:id 3718 :firstname "Nicholas" :lastname "Davis" :birthdate "1936-01-05" :city "Vientiane"}
   {:id 3727 :firstname "Jennifer" :lastname "Parker" :birthdate "1988-11-24" :city "Kampala"}
   {:id 3736 :firstname "Lisa" :lastname "Clark" :birthdate "1981-02-24" :city "Kabul"}
   {:id 3739 :firstname "Courtney" :lastname "Cox" :birthdate "1938-10-12" :city "Reykjavík"}
   {:id 3744 :firstname "Jamie" :lastname "Nelson" :birthdate "1950-03-07" :city "Madrid"}
   {:id 3754 :firstname "Jenna" :lastname "Turner" :birthdate "1970-01-19" :city "Taipei"}
   {:id 3764 :firstname "Andrea" :lastname "Cruz" :birthdate "1931-11-20" :city "Vientiane"}
   {:id 3769 :firstname "Rachel" :lastname "Carter" :birthdate "1944-09-18" :city "Monaco"}
   {:id 3770 :firstname "Timothy" :lastname "Perez" :birthdate "1943-09-15" :city "Thimphu"}
   {:id 3778 :firstname "Danielle" :lastname "Mitchell" :birthdate "1951-09-26" :city "Skopje"}
   {:id 3784 :firstname "Jacqueline" :lastname "Williams" :birthdate "1956-02-16" :city "Muscat"}
   {:id 3788 :firstname "Julie" :lastname "Campbell" :birthdate "1947-04-08" :city "Kinshasa"}
   {:id 3798 :firstname "Sarah" :lastname "Thomas" :birthdate "1997-02-24" :city "Prague"}
   {:id 3807 :firstname "Brian" :lastname "Lopez" :birthdate "1932-02-08" :city "Kingstown"}
   {:id 3811 :firstname "Charles" :lastname "Peterson" :birthdate "1970-12-16" :city "Wellington"}
   {:id 3821 :firstname "Holly" :lastname "Lewis" :birthdate "1985-05-01" :city "Minsk"}
   {:id 3826 :firstname "Allison" :lastname "Martin" :birthdate "1981-05-17" :city "Hanga Roa"}
   {:id 3829 :firstname "Stephen" :lastname "Diaz" :birthdate "1982-08-15" :city "Maseru"}
   {:id 3835 :firstname "Cassandra" :lastname "Smith" :birthdate "1961-08-06" :city "Lima"}
   {:id 3844 :firstname "Edward" :lastname "Moore" :birthdate "1950-01-02" :city "Chisinau"}
   {:id 3846 :firstname "Sarah" :lastname "Turner" :birthdate "1977-04-25" :city "Pyongyang"}
   {:id 3850 :firstname "Adam" :lastname "Collins" :birthdate "1994-03-28" :city "Valletta"}
   {:id 3852 :firstname "Charles" :lastname "Robinson" :birthdate "1993-07-07" :city "Kuwait City"}
   {:id 3856 :firstname "April" :lastname "Martinez" :birthdate "1938-04-04" :city "Victoria"}
   {:id 3863 :firstname "Nicholas" :lastname "Brown" :birthdate "1939-07-21" :city "Reykjavík"}
   {:id 3864 :firstname "Krystal" :lastname "Ramirez" :birthdate "1971-11-16" :city "Saipan"}
   {:id 3870 :firstname "Christine" :lastname "Cook" :birthdate "1927-12-20" :city "Pristina"}
   {:id 3876 :firstname "Anna" :lastname "Cooper" :birthdate "1988-06-01" :city "Kampala"}
   {:id 3886 :firstname "Katie" :lastname "Hall" :birthdate "1946-11-16" :city "Tokyo"}
   {:id 3895 :firstname "Danielle" :lastname "Moore" :birthdate "1956-03-16" :city "London"}
   {:id 3899 :firstname "Samantha" :lastname "Miller" :birthdate "1993-10-25" :city "Reykjavík"}
   {:id 3902 :firstname "Stephanie" :lastname "Rogers" :birthdate "1976-01-11" :city "Copenhagen"}
   {:id 3908 :firstname "Marcus" :lastname "Turner" :birthdate "1968-08-24" :city "Hamilton"}
   {:id 3911 :firstname "William" :lastname "Davis" :birthdate "1950-10-01" :city "Victoria"}
   {:id 3921 :firstname "Kyle" :lastname "Taylor" :birthdate "1959-11-18" :city "Road Town"}
   {:id 3925 :firstname "Tara" :lastname "Brown" :birthdate "1987-09-17" :city "Kathmandu"}
   {:id 3927 :firstname "Stephanie" :lastname "Williams" :birthdate "1931-12-20" :city "Niamey"}
   {:id 3931 :firstname "John" :lastname "Robinson" :birthdate "1961-02-27" :city "Doha"}
   {:id 3933 :firstname "Cassandra" :lastname "Stewart" :birthdate "1940-07-02" :city "Caracas"}
   {:id 3940 :firstname "Mark" :lastname "Johnson" :birthdate "1993-12-17" :city "Mogadishu"}
   {:id 3943 :firstname "Michael" :lastname "Diaz" :birthdate "1952-05-24" :city "Vienna"}
   {:id 3950 :firstname "Christina" :lastname "Nielsen" :birthdate "1975-03-25" :city "Luxembourg"}
   {:id 3960 :firstname "Thomas" :lastname "Parker" :birthdate "1978-01-23" :city "Kingston"}
   {:id 3964 :firstname "Julie" :lastname "Nelson" :birthdate "1971-01-08" :city "Canberra"}
   {:id 3971 :firstname "Charles" :lastname "Edwards" :birthdate "1985-05-22" :city "Kingston"}
   {:id 3974 :firstname "Jenna" :lastname "Nelson" :birthdate "1992-11-08" :city "London"}
   {:id 3978 :firstname "Crystal" :lastname "Wright" :birthdate "1940-04-14" :city "Ottawa"}
   {:id 3984 :firstname "Justin" :lastname "Lee" :birthdate "1928-06-15" :city "Mogadishu"}
   {:id 3991 :firstname "Alexander" :lastname "Lee" :birthdate "1929-01-05" :city "Luxembourg"}])
  

(defn list-all 
  []
  (str/join "\n"
    (map #(format "%3d %s %s" (% :id) (% :firstname) (% :lastname)) data)))

(defn entry
  [id]
  (when-let [entry (first (filter #(= (% :id) id) data))]
    (format
      (str "\n"
           "ID:        %d\n"
           "FIRSTNAME: %s\n"
           "LASTNAME:  %s\n"
           "BIRTHDATE: %s\n"
           "CITY:      %s\n")
      (entry :id)
      (entry :firstname)
      (entry :lastname)
      (entry :birthdate)
      (entry :city))))

(defn index
  []
  (str "
                                  Contacts

--------------------------------------------------------------------------------

To list all contacts use:

    <host>/contacts/list

To display entry with id use:

    <host>/contacts/<id>

  "))

(defn api
  [& args]
  (cond (nil? (first args)) (index)
        (= (first args) "list") (list-all)
        (re-matches #"\d+" (first args)) (entry (Integer/parseInt (first args)))
        true "Invalid"))

(comment (println (index)))
(comment (println (entry 3)))
