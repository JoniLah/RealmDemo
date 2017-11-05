package com.example.joni.realmdemo;

/**
 * Created by Joni on 25.8.2017.
 */

public class CountryCheck {

    public String returnCountry(String countryCode) {
        return countriesListed(countryCode);
    }

    public String countriesListed(String countryCode) {
        String country = "";
        int code = Integer.parseInt(countryCode);
        switch(code) {
            // USA
            case 1: country = "USA"; break;

            // Africa
            case 20: country = "Egypt"; break;
            case 211: country = "South Sudan"; break;
            case 212: country = "Morocco"; break;
            case 213: country = "Algeria"; break;
            case 216: country = "Tunisia"; break;
            case 218: country = "Libya"; break;
            case 220: country = "Gambia"; break;
            case 221: country = "Senegal"; break;
            case 222: country = "Mauritania"; break;
            case 223: country = "Mali"; break;
            case 224: country = "Guinea"; break;
            case 225: country = "Ivory Coast"; break;
            case 226: country = "Burkina Faso"; break;
            case 227: country = "Niger"; break;
            case 228: country = "Togo"; break;
            case 229: country = "Benin"; break;
            case 230: country = "Mauritius"; break;
            case 231: country = "Liberia"; break;
            case 232: country = "Sierra Leone"; break;
            case 233: country = "Ghana"; break;
            case 234: country = "Nigeria"; break;
            case 235: country = "Chad"; break;
            case 236: country = "Central African Republic"; break;
            case 237: country = "Cameroon"; break;
            case 238: country = "Cape Verde"; break;
            case 239: country = "São Tomé and Príncipe"; break;
            case 240: country = "Equatorial Guinea"; break;
            case 241: country = "Gabon"; break;
            case 242: country = "Republic of the Congo"; break;
            case 243: country = "Democratic Republic of the Congo"; break;
            case 244: country = "Angola"; break;
            case 245: country = "Guinea-Bissau"; break;
            case 246: country = "British Indian Ocean Territory"; break;
            case 247: country = "Ascension Island"; break;
            case 248: country = "Seychelles"; break;
            case 249: country = "Sudan"; break;
            case 250: country = "Rwanda"; break;
            case 251: country = "Ethiopia"; break;
            case 252: country = "Somalia"; break;
            case 253: country = "Djibouti"; break;
            case 254: country = "Kenya"; break;
            case 255: country = "Tanzania"; break;
            case 256: country = "Uganda"; break;
            case 257: country = "Burundi"; break;
            case 258: country = "Mozambique"; break;
            case 260: country = "Zambia"; break;
            case 261: country = "Madagascar"; break;
            case 262: country = "Réunion"; break;
            case 263: country = "Zimbabwe"; break;
            case 264: country = "Namibia"; break;
            case 265: country = "Malawi"; break;
            case 266: country = "Lesotho"; break;
            case 267: country = "Botswana"; break;
            case 268: country = "Swaziland"; break;
            case 269: country = "Comoros"; break;
            case 27: country = "South Africa"; break;
            case 290: country = "Saint Helena"; break;
            case 291: country = "Eritrea"; break;
            case 297: country = "Aruba"; break;
            case 298: country = "Faroe Islands"; break;
            case 299: country = "Greenland"; break;

            // Europe
            case 30: country = "Greece"; break;
            case 31: country = "Netherlands"; break;
            case 32: country = "Belgium"; break;
            case 33: country = "France"; break;
            case 34: country = "Spain"; break;
            case 350: country = "Gibraltar"; break;
            case 351: country = "Portugal"; break;
            case 352: country = "Luxembourg"; break;
            case 353: country = "Ireland"; break;
            case 354: country = "Iceland"; break;
            case 355: country = "Albania"; break;
            case 356: country = "Malta"; break;
            case 357: country = "Cyprus"; break;
            case 358: country = "Finland"; break;
            case 359: country = "Bulgaria"; break;
            case 36: country = "Hungary"; break;
            case 370: country = "Lithuania"; break;
            case 371: country = "Latvia"; break;
            case 372: country = "Estonia"; break;
            case 373: country = "Moldova"; break;
            case 374: country = "Armenia"; break;
            case 375: country = "Belarus"; break;
            case 376: country = "Andorra"; break;
            case 377: country = "Monaco"; break;
            case 378: country = "San Marino"; break;
            case 379: country = "Vatican City"; break;
            case 380: country = "Ukraine"; break;
            case 381: country = "Serbia"; break;
            case 382: country = "Montenegro"; break;
            case 383: country = "Kosovo"; break;
            case 385: country = "Croatia"; break;
            case 386: country = "Slovenia"; break;
            case 387: country = "Bosnia and Herzegovina"; break;
            case 389: country = "Macedonia"; break;
            case 39: country = "Italy"; break;
            case 40: country = "Romania"; break;
            case 41: country = "Switzerland"; break;
            case 420: country = "Czech Republic"; break;
            case 421: country = "Slovakia"; break;
            case 423: country = "Liechtenstein"; break;
            case 43: country = "Austria"; break;
            case 44: country = "United Kingdom"; break;
            case 45: country = "Denmark"; break;
            case 46: country = "Sweden"; break;
            case 47: country = "Norway"; break;
            case 48: country = "Poland"; break;
            case 49: country = "Germany"; break;

            //Lower North America, Central America and South America
            case 500: country = "Falkland Islands"; break;
            case 501: country = "Belize"; break;
            case 502: country = "Guatemala"; break;
            case 503: country = "El Salvador"; break;
            case 504: country = "Honduras"; break;
            case 505: country = "Nicaragua"; break;
            case 506: country = "Costa Rica"; break;
            case 507: country = "Panama"; break;
            case 508: country = "Saint-Pierre and Miquelon"; break;
            case 509: country = "Haiti"; break;
            case 51: country = "Peru"; break;
            case 52: country = "Mexico"; break;
            case 53: country = "Cuba"; break;
            case 54: country = "Argentina"; break;
            case 55: country = "Brazil"; break;
            case 56: country = "Chile"; break;
            case 57: country = "Colombia"; break;
            case 58: country = "Venezuela"; break;
            case 590: country = "Guadeloupe"; break;
            case 591: country = "Bolivia"; break;
            case 592: country = "Guyana"; break;
            case 593: country = "Ecuador"; break;
            case 594: country = "French Guiana"; break;
            case 595: country = "Paraguay"; break;
            case 596: country = "Martinique"; break;
            case 597: country = "Suriname"; break;
            case 598: country = "Uruguay"; break;
            case 599: country = "Sint Eustatius"; break;

            // Southeast Asia and Oceania
            case 60: country = "Malaysia"; break;
            case 61: country = "Australia"; break;
            case 62: country = "Indonesia"; break;
            case 63: country = "Philippines"; break;
            case 64: country = "New Zealand"; break;
            case 65: country = "Singapore"; break;
            case 66: country = "Thailand"; break;
            case 670: country = "East Timor"; break;
            case 672: country = "Australian External Terrories"; break;
            case 673: country = "Brunei"; break;
            case 674: country = "Nauru"; break;
            case 675: country = "Papua New Guinea"; break;
            case 676: country = "Tonga"; break;
            case 677: country = "Solomon Islands"; break;
            case 678: country = "Vanuatu"; break;
            case 679: country = "Fiji"; break;
            case 680: country = "Palau"; break;
            case 681: country = "Wallis and Futuna"; break;
            case 682: country = "Cook Islands"; break;
            case 683: country = "Niue"; break;
            case 685: country = "Samoa"; break;
            case 686: country = "Kiribati"; break;
            case 687: country = "New Caledonia"; break;
            case 688: country = "Tuvalu"; break;
            case 689: country = "French Polynesia"; break;
            case 690: country = "Tokelau"; break;
            case 691: country = "Federated States of Micronesia"; break;
            case 692: country = "Marshall Islands"; break;

            // Russia
            case 7: country = "Russia"; break;

            // East Asia
            case 81: country = "Japan"; break;
            case 82: country = "South Korea"; break;
            case 84: country = "Vietnam"; break;
            case 850: country = "North Korea"; break;
            case 852: country = "Hong Kong"; break;
            case 853: country = "Macau"; break;
            case 855: country = "Cambodia"; break;
            case 856: country = "Laos"; break;
            case 86: country = "China"; break;
            case 880: country = "Bangladesh"; break;
            case 886: country = "Taiwan"; break;

            // Asia
            case 90: country = "Turkey"; break;
            case 91: country = "India"; break;
            case 92: country = "Pakistan"; break;
            case 93: country = "Afghanistan"; break;
            case 94: country = "Sri Lanka"; break;
            case 95: country = "Myanmar"; break;
            case 960: country = "Maldives"; break;
            case 961: country = "Lebanon"; break;
            case 962: country = "Jordan"; break;
            case 963: country = "Syria"; break;
            case 964: country = "Iraq"; break;
            case 965: country = "Kuwait"; break;
            case 966: country = "Saudi Arabia"; break;
            case 967: country = "Yemen"; break;
            case 968: country = "Oman"; break;
            case 970: country = "Palestine"; break;
            case 971: country = "United Arab Emirates"; break;
            case 972: country = "Israel"; break;
            case 973: country = "Bahrain"; break;
            case 974: country = "Qatar"; break;
            case 975: country = "Bhutan"; break;
            case 976: country = "Mongolia"; break;
            case 977: country = "Nepal"; break;
            case 98: country = "Iran"; break;
            case 992: country = "Tajikistan"; break;
            case 993: country = "Turkmenistan"; break;
            case 994: country = "Azerbaijan"; break;
            case 995: country = "Georgia"; break;
            case 996: country = "Kyrgyzstan"; break;
            case 998: country = "Uzbekistan"; break;


            default: country = "NULL"; break;
        }
        return country;
    }
}