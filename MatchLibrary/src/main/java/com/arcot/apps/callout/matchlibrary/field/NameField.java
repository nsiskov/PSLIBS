package com.arcot.apps.callout.matchlibrary.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.arcot.apps.callout.common.COConfig;
import com.arcot.apps.callout.common.CORequest;
import com.arcot.apps.callout.matchlibrary.util.Constants;
import com.arcot.apps.callout.matchlibrary.util.MatchException;
import com.arcot.apps.callout.util.StringUtil;
import com.arcot.callout.CallOutsConfig;

public class NameField extends Field {

	private final String PARTIAL_PERCENTAGE = "P";
	
	public NameField(CORequest request, COConfig config, String fieldName, String userData, String storedData) {
		super(request, config, fieldName, userData, storedData);
	}

	@Override
	public boolean matchEncrypted(String userData, String userDataFormat, String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		HashMap<String, String> flagsMap = getMatchFormatFlagsMap(matchFormat);
		
		if(matchWithFlags(userData, storedData, flagsMap)) {
			return true;
		} else {
			return matchPercentage(userData, storedData, flagsMap);
		}
	}
	
	@Override
	public boolean matchHashed(String userData, String userDataFormat,
			String storedData, String storedDataFormat, String matchFormat) throws MatchException {
		// TODO Auto-generated method stub
		return false;
	}
	
	private boolean matchWithFlags(String userData, String storedData, HashMap<String, String> flagsMap) {
		if(userData.equals(storedData)) {
			log("Exact match successfull.");
			return true;
		}
		
		boolean ignoreCase = getFlag(flagsMap, Constants.CONF_IGNORECASE_FLAG, defaultIgnoreCase());
		boolean ignoreSplChars = getFlag(flagsMap, Constants.CONF_IGNORE_SPLCHARS_FLAG, defaultIgnoreSplChars());
		boolean ignoreSpaces = getFlag(flagsMap, Constants.CONF_IGNORE_SPACES_FLAG, defaultIgnoreSpaces());
		boolean shuffle = getFlag(flagsMap, Constants.CONF_SHUFFLE_FLAG, defaultShuffleFlag());
		boolean ignoreTitle = getFlag(flagsMap, Constants.CONF_IGNORE_TITLE_FLAG, defaultIgnoreTitle());
		
		boolean ignoreNonAlphaNumerics = getFlag(flagsMap, Constants.CONF_IGNORE_NONALPHANUMERICS, defaultIgnoreNonAlphaNumerics());
		boolean ignoreNumerics = getFlag(flagsMap, Constants.CONF_IGNORE_NUMERICS, defaultIgnoreNonNumerics());
		
		debug("Flags : IgnoreCase=" +ignoreCase + ", IgnoreSpecialCharacters=" +ignoreSplChars + ", IgnoreSpaces=" +ignoreSpaces + ", IgnoreTitle=" +ignoreTitle + ", Shuffle=" +shuffle);
		
		if(ignoreCase) {
			info("Case insensitive match. Changing inputs to lower case.");
			userData = userData.toLowerCase();
			storedData = storedData.toLowerCase();
			if(userData.equals(storedData)) {
				log("Case insensitive match successfull.");
				return true;
			}
		}
		
		String userDataWithoutTitle = null, storedDataWithoutTitle = null;
		if(ignoreTitle) {
			info("Ignoring title.");
			userDataWithoutTitle = removeTitle(userData);
			storedDataWithoutTitle = removeTitle(storedData);
			if(userDataWithoutTitle.equals(storedDataWithoutTitle)) {
				return true;
			}
		}
		
		if(ignoreNonAlphaNumerics) {
			String userDataWoAlphaNum = StringUtil.removeNonAlphaNumerics(userData, new char[]{' '});
			String storedDataWoAlphaNum = StringUtil.removeNonAlphaNumerics(storedData, new char[]{' '});
			if(userDataWoAlphaNum.equals(storedDataWoAlphaNum)) {
				return true;
			}
		} else {
			if(ignoreSplChars) {
				info("Ignoring Special characters.");
				userData = StringUtil.removeChars(userData, getSplCharArray());
				storedData = StringUtil.removeChars(storedData, getSplCharArray());
				if(userData.equals(storedData)) {
					return true;
				}
			}
			if(ignoreNumerics) {
				String userDataWoNum = StringUtil.removeNumerics(userData);
				String storedDataWoNum = StringUtil.removeNumerics(storedData);
				if(userDataWoNum.equals(storedDataWoNum)) {
					return true;
				}
			}
		}
		
		if(ignoreSpaces) {
			info("Ignoring Spaces.");
			String userDataWithoutSpaces = StringUtil.removeChar(userData, ' ');
			String storedDataWithoutSpaces = StringUtil.removeChar(storedData, ' ');
			if(userDataWithoutSpaces.equals(storedDataWithoutSpaces))
				return true;
			if(ignoreTitle) {
				String userDataWithoutSpacesTitle = StringUtil.removeChar(userDataWithoutTitle, ' ');
				String storedDataWithoutSpacesTitle = StringUtil.removeChar(storedDataWithoutTitle, ' ');
				if(userDataWithoutSpacesTitle.equals(storedDataWithoutSpacesTitle))
					return true;
			}
		}
		
		if(shuffle) {
			info("Shuffle enabled.");
			String sortedInputArray[] = getSortedSubStringArray(userData, Constants.NAME_DELIM);
			String sortedStoredArray[] = getSortedSubStringArray(storedData, Constants.NAME_DELIM);
			String userDataShuffled = combineSortedSubStrings(sortedInputArray);
			String storedDataShuffled = combineSortedSubStrings(sortedStoredArray);
			if(userDataShuffled.equals(storedDataShuffled))
				return true;
		}
		return false;
	}
	
	protected boolean matchPercentage(String userData, String storedData, HashMap<String, String> flagsMap) throws MatchException {
		int partialMatchPercentage = -1;

		String partialMatchFormat = flagsMap.get("default");
		if(!StringUtil.isBlank(partialMatchFormat)) {
			String partialFormatStyle = String.valueOf(partialMatchFormat.charAt(0));
			partialMatchFormat = partialMatchFormat.substring(1);

			if(!partialFormatStyle.equalsIgnoreCase(PARTIAL_PERCENTAGE))
				throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "MatchFormat(MF) should start with P for Numeric fields. Only percentage match allowed.");
			try {
				partialMatchPercentage = Integer.parseInt(partialMatchFormat);
			} catch (NumberFormatException e1) {
				throw new MatchException(Constants.ERR_INVALID_MATCHFORMAT, "MatchFormat(MF) should be have a number for Numeric fields.");
			}
		}
		
		log("Percentage matched configured :"+ partialMatchPercentage);

		NameField.Name userName = null;
		NameField.Name storedName = null;
		try {
			userName = this.new Name(userData);
			storedName = this.new Name(storedData);
		} catch (MatchException e) {
			return false;
		}
		
		if(partialMatchPercentage != -1) {
			int matchPercentage = userName.compareNames(storedName, flagsMap);
			int matchPercentage2 = storedName.compareNames(userName, flagsMap);
			log("Match percentage :"+ matchPercentage + " : "+ matchPercentage2);
			if(partialMatchPercentage < matchPercentage) {
				log("Match percentage "+ matchPercentage+ " greater than the configured percentage "+ partialMatchPercentage + ". Returning true");
				return true;
			}
		}
		
		return false;
	}

	private String removeTitle(String name) {
		int nameTitleEndIndex = name.indexOf(Constants.NAME_DELIM);
		String title = null;
		if(nameTitleEndIndex > 0) 
			title = name.substring(0, nameTitleEndIndex);
		String nameWithoutTitle = name;
		if(isTitle(title))
			nameWithoutTitle = name.substring(nameTitleEndIndex+1);
		debug(name+" : Name after removing title :"+nameWithoutTitle);
		return nameWithoutTitle;
	}
	
	private String[] getSortedSubStringArray(String input, String delim) {
		String[] inputArray = input.split(delim);
		Arrays.sort(inputArray);
		return inputArray;
	}
	
	private String combineSortedSubStrings(String[] sortedArray) {
		StringBuilder tempInput = new StringBuilder();
		for(String name:sortedArray)
			tempInput.append(" "+name);
		return tempInput.toString().substring(1);
	}
	
	private boolean isTitle(String name) {
		if(StringUtil.isBlank(getTitleGroupName(name)))
			return false;
		return true;
	}
	
	private String getTitleGroupName(String name) {
		if(StringUtil.isBlank(name))
			return null;
		
		Iterator<String> iter = defaultTitleMap.keySet().iterator();
		while(iter.hasNext()) {
			String titleGroupName = iter.next();
			List<String> titleGroup = defaultTitleMap.get(titleGroupName);
			
			if(titleGroup.contains(name) || titleGroup.contains(name.toUpperCase()))
				return titleGroupName;
		}
		
		return null;
	}
	
	@Override
	public String getUserDataFormatFromConfig() throws MatchException {
		return "-1";
	}
	
	@Override
	public String getStoredDataFormatFromConfig() throws MatchException {
		return "-1";
	}

	private static Map<String, List<String>> defaultTitleMap = null;
	
	static {
		//TODO Provide an option to get these title groups from Config/ESCache or property file. Increasing groups should be possible
		int totalSets = 4;
		
		String set1 = "MR, Mr, mr, MR., Mr., mr., MISTER, Mister, mister, MISTER., Mister., mister.";
		String set2 = "DR, Dr, dr, DR., Dr., dr., DOCTOR, Doctor, doctor, DOCTOR., Doctor., doctor.";
		String set3 = "SR, Sr, sr, SR., Sr., sr., SENIOR, Senior, senior, SENIOR., Senior., senior.";
		String set4 = "MAJOR, Major, major, MAJOR., Major., major.";
		
		defaultTitleMap = new HashMap<String, List<String>>(totalSets);

		defaultTitleMap.put("Set1", parseTitles(set1));
		defaultTitleMap.put("Set2", parseTitles(set2));
		defaultTitleMap.put("Set3", parseTitles(set3));
		defaultTitleMap.put("Set4", parseTitles(set4));
	}
	
	public static List<String> parseTitles(String titleString) {
		String[] titles = titleString.split(",");
		List<String> list = new ArrayList<String>();
		for(String title : titles) {
			list.add(title.trim());
		}
		
		return list;
	}
	
	public static void main(String[] args) {
		String infoList = "LogicHandler=com.arcot.apps.test.blh.TestBusinessLogicHandler;" +
		"LogFilePath=C:\\TestValidator;" +
		"CN_MF=IGNORESPACES|IGNORESPLCHARS|IGNORETITLE|PMP=50;" +
		"MATCHLIB_LOG_LEVEL=DEBUG;";
		
		CallOutsConfig ccConfig = new CallOutsConfig();
		ccConfig.setInfoList(infoList);
		COConfig config = new COConfig(ccConfig);
		
		String nameOne = "Mr Mahinder Singh Dhoni";
		String nameTwo = "Mister. Dhoni";
		
		NameField field = null;
		NameField.Name name1 = null;
		NameField.Name name2 = null;
		HashMap<String, String> flagsMap = null;
		try {
			field = new NameField(null, config, "CHN", "Mahnder Singh Dhoni", "M S DHONI");
			name1 = field.new Name(nameOne);
			name2 = field.new Name(nameTwo);
			
			/*name1 = field.new Name("Mr Veera V Sai Laxman K");
			name2 = field.new Name("Mister. Veera Venkata Sai Laxman Kumar");*/
			
			flagsMap = field.getMatchFormatFlagsMap("IGNORESPACES-y|IGNORESPLCHARS-y|IGNORETITLE-N|50;");
		} catch (MatchException e) {
			e.printStackTrace();
		}
		
		System.out.println(name1);
		System.out.println(name2);
		int result = name1.compareNames(name2, flagsMap);
		field.debug("Name1 :"+ nameOne + " and Name2 :"+ nameTwo + " :"+ result);
	}
	
	private class Name {
		
		private String title = "";
		private String firstName = "";
		private String middleName = "";
		private String lastName = "";
		
		public Name(String name) throws MatchException {
			parseName(name);
		}
		
		public String getTitle() {
			return title;
		}
		public String getFirstName() {
			return firstName;
		}
		public String getMiddleName() {
			return middleName;
		}
		public String getLastName() {
			return lastName;
		}
		
		private void parseName(String name) throws MatchException {
			if(StringUtil.isBlank(name))
				throw new MatchException("INVALID_NAME", "Name is null or blank.");
			
			String[] names = name.split(Constants.NAME_DELIM);
			
			if(names == null || names.length == 0)
				throw new MatchException("INVALID_NAME", "Name is blank.");
				
			if(!StringUtil.isBlank(getTitleGroupName(names[0]))) {
				this.title = names[0];
				setNames(names, true);
			} else {
				setNames(names, false);
			}
		}
		
		private void setNames(String[] names, boolean hasTitle) throws MatchException {
			int i = 0;
			if(hasTitle) {
				i = 1;
			}
			
			if(i + 3 < names.length) {
				this.lastName = names[names.length - 1];
				this.middleName = names[names.length - 2];
				
				StringBuilder sb = new StringBuilder();
				for(int j = i; j < names.length - 2 ; j++) {
					sb.append(" " + names[j]);
				}
				this.firstName = sb.toString().substring(1);
			} else if(i + 3 == names.length) {
				this.lastName = names[names.length - 1];
				this.middleName = names[names.length - 2];
				this.firstName = names[names.length - 3];
			} else if(i + 2 == names.length) {
				this.lastName = names[names.length - 1];
				this.firstName = names[names.length - 2];
			} else if(i + 1 == names.length) {
				this.firstName = names[names.length - 1];
			} else if(i == names.length) {
				throw new MatchException("INVALID_NAME", "Name entered has only title.");
			}
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Title :"+ title + ", ");
			sb.append("First Name :"+ firstName + ", ");
			sb.append("Middle Name :"+ middleName + ", ");
			sb.append("Last Name :"+ lastName);
			return sb.toString();
		}
		
		public int compareNames(Name name, HashMap<String, String> flagsMap) {
			log("Comparing names.");

			boolean ignoreCase = getFlag(flagsMap, Constants.CONF_IGNORECASE_FLAG, defaultIgnoreCase());
			boolean ignoreTitle = getFlag(flagsMap, Constants.CONF_IGNORE_TITLE_FLAG, defaultIgnoreTitle());
			
			int titleComp = 0;
			int firstNameComp = 0;
			int middleNameComp = 0;
			int lastNameComp = 0;

			if(ignoreCase) {
				titleComp = compareTitles(this.getTitle().toLowerCase(), name.getTitle().toLowerCase(), ignoreTitle);
				firstNameComp = compareNameParts(this.getFirstName().toLowerCase(), name.getFirstName().toLowerCase());
				middleNameComp = compareNameParts(this.getMiddleName().toLowerCase(), name.getMiddleName().toLowerCase());
				lastNameComp = compareNameParts(this.getLastName().toLowerCase(), name.getLastName().toLowerCase());
			} else {
				titleComp = compareTitles(this.getTitle(), name.getTitle(), ignoreTitle);
				firstNameComp = compareNameParts(this.getFirstName(), name.getFirstName());
				middleNameComp = compareNameParts(this.getMiddleName(), name.getMiddleName());
				lastNameComp = compareNameParts(this.getLastName(), name.getLastName());
			}
			
			log("Title Comparision :"+ titleComp + ", First Name Comparision :"+ firstNameComp + ", Middle Name Comparision :"+ middleNameComp + " and Last Name Comparision :"+ lastNameComp);
			
			if(titleComp == -1 || firstNameComp == -1 || middleNameComp == -1 || lastNameComp == -1) {
				log("One of the names are different. Complete match : 0");
				return 0;
			}
			
			return (titleComp + middleNameComp + firstNameComp + lastNameComp)/4;
		}
		
		private int compareNameParts(String namePart1, String namePart2) {
			debug("Comparing name parts :"+ namePart1 + " and :"+ namePart2);
			if(StringUtil.isBlank(namePart1) && StringUtil.isBlank(namePart2)) {
				log("Null name part for both names. Returning 100.");
				return 100;
			}
			if(StringUtil.isBlank(namePart1) && !StringUtil.isBlank(namePart2)) {
				log("Name part 1 is null or blank. Returning 25");
				return 0;
			}
			if(!StringUtil.isBlank(namePart1) && StringUtil.isBlank(namePart2)) {
				log("Name part 2 is null or blank. Returning 25");
				return 0;
			}
			
			
			if(namePart1.equals(namePart2)) {
				log("Name part same for both the names. Returning 100.");
				return 100;
			}
			
			if(namePart1.length() == 1 && namePart2.startsWith(namePart1)) {
				log("Name part 1 has only 1 character which is the starting character of Name part 2. Partial match, returning 50");
				return 50;
			}
			
			if(namePart2.length() == 1 && namePart1.startsWith(namePart1)) {
				log("Name part 2 has only 1 character which is the starting character of Name part 1 . Partial match, returning 50");
				return 50;
			}

			// For handling a situation where the first name has Abbreviations.. like "V V S Laxman Kumar" for "Veera Venkata Sai Laxman Kumar". First names would be "Veera Venkata Sai" and "V V S".
			if(namePart1.indexOf(Constants.NAME_DELIM) > -1 && namePart2.indexOf(Constants.NAME_DELIM) > -1) {
				return compareMultipleParts(namePart1, namePart2);
			}
			
			log("No scenario matched. Returning : -1");
			return -1;
		}
		
		public int compareMultipleParts(String namePart1, String namePart2) {
			String names1[] = namePart1.split(Constants.NAME_DELIM);
			String names2[] = namePart2.split(Constants.NAME_DELIM);

			log("FirstName entered has multiple parts. Checking if the entered values are with abbreviations.");
			if(names1.length == names2.length) {
				int result = 0;
				for(int i = 0; i< names1.length; i++) {
					if(names1[i].equals(names2[i])) {
						log("Name part same for both the names. Returning 100.");
						result += 100;
					} else if(names1[i].length() == 1 && names2[i].startsWith(names1[i])) {
						log("FirstName has only 1 character which is the starting character of second name. Partial match, returning 50");
						result+=50;
					} else if(names2[i].length() == 1 && names1[i].startsWith(names2[i])) {
						log("Second name has only 1 character which is the starting character of first name. Partial match, returning 50");
						result+=50;
					} else {
						log("One of the parts is not same. Returning -1");
						return -1;
					}
				}
				return result/names1.length;
			} else {
				log("FirstName entered has multiple parts, but count entered is not same in both values entered.");
				return -1;
			}
		}

		public int compareTitles(String title1, String title2, boolean ignoreTitle) {
			if(!ignoreTitle) {
				log("Ignore Title flag :"+ ignoreTitle);
				debug("Validating titles :"+ title1 + " and " + title2);
				
				if(StringUtil.isBlank(title1) && StringUtil.isBlank(title2)) {
					log("Both the names are without title. Returning 100.");
					return 100;
				} else if(!StringUtil.isBlank(title1) && StringUtil.isBlank(title2)) {
					log("Title 2 is null or blank.");
					return 0;
				} else if(StringUtil.isBlank(title1) && !StringUtil.isBlank(title2)) {
					log("Title 1 is null or blank.");
					return 0;
				}
				
				if((title1.equals(title2))) {
					log("Both the names have same title. Returning 100.");
					return 100;
				}
				
				String group1 = getTitleGroupName(title1);
				String group2 = getTitleGroupName(title1);
				
				if(group1.equals(group2)) {
					log("Both the titles are from same title group. Returning 100.");
					return 100;
				}
				
				return 0;
			} else {
				log("Title ignored.");
				return 100;
			}
		}
		
	}
	
	@Override
	protected boolean defaultIgnoreCase() { 
		return true;
	}
	
	@Override
	protected boolean defaultIgnoreSplChars() { 
		return true;
	}
	
	@Override
	protected boolean defaultIgnoreSpaces() { 
		return false;
	}
	
	@Override
	protected boolean defaultIgnoreTitle() {
		return true;
	}
	
	@Override
	protected boolean defaultShuffleFlag() {
		return false;
	}


}
