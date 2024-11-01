    * 
    * @param job
    * @param enable
    */
   public static void setIsolated(JobContext job, boolean enable) {
    Configuration conf = job.getConfiguration();
     conf.setBoolean(ISOLATED, enable);
   }
   
    * 
    * @param job
    * @param enable
    */
   public static void setLocalIterators(JobContext job, boolean enable) {
    Configuration conf = job.getConfiguration();
     conf.setBoolean(LOCAL_ITERATORS, enable);
   }
   
   public static void setInputInfo(JobContext job, String user, byte[] passwd, String table, Authorizations auths) {
    Configuration conf = job.getConfiguration();
     if (conf.getBoolean(INPUT_INFO_HAS_BEEN_SET, false))
       throw new IllegalStateException("Input info can only be set once per job");
     conf.setBoolean(INPUT_INFO_HAS_BEEN_SET, true);
       conf.set(AUTHORIZATIONS, auths.serialize());
   }
   
   public static void setZooKeeperInstance(JobContext job, String instanceName, String zooKeepers) {
    Configuration conf = job.getConfiguration();
     if (conf.getBoolean(INSTANCE_HAS_BEEN_SET, false))
       throw new IllegalStateException("Instance info can only be set once per job");
     conf.setBoolean(INSTANCE_HAS_BEEN_SET, true);
     conf.set(ZOOKEEPERS, zooKeepers);
   }
   
   public static void setMockInstance(JobContext job, String instanceName) {
    Configuration conf = job.getConfiguration();
     conf.setBoolean(INSTANCE_HAS_BEEN_SET, true);
     conf.setBoolean(MOCK, true);
     conf.set(INSTANCE_NAME, instanceName);
   }
   
   public static void setRanges(JobContext job, Collection<Range> ranges) {
     ArgumentChecker.notNull(ranges);
     ArrayList<String> rangeStrings = new ArrayList<String>(ranges.size());
     try {
     } catch (IOException ex) {
       throw new IllegalArgumentException("Unable to encode ranges to Base64", ex);
     }
    job.getConfiguration().setStrings(RANGES, rangeStrings.toArray(new String[0]));
   }
   
   public static void disableAutoAdjustRanges(JobContext job) {
    job.getConfiguration().setBoolean(AUTO_ADJUST_RANGES, false);
   }
   
   public static enum RegexType {
     ROW, COLUMN_FAMILY, COLUMN_QUALIFIER, VALUE
   }
   
   /**
   * @deprecated since 1.4 {@link #addIterator(JobContext, IteratorSetting)}
    * @see org.apache.accumulo.core.iterators.user.RegExFilter#setRegexs(IteratorSetting, String, String, String, String, boolean)
    * @param job
    * @param type
    * @param maxVersions
    *          the max versions
    * @throws IOException
    */
   public static void setMaxVersions(JobContext job, int maxVersions) throws IOException {
     if (maxVersions < 1)
       throw new IOException("Invalid maxVersions: " + maxVersions + ".  Must be >= 1");
    job.getConfiguration().setInt(MAX_VERSIONS, maxVersions);
   }
   
   /**
    * 
    * @param columnFamilyColumnQualifierPairs
    *          A pair of {@link Text} objects corresponding to column family and column qualifier. If the column qualifier is null, the entire column family is
    *          selected. An empty set is the default and is equivalent to scanning the all columns.
    */
   public static void fetchColumns(JobContext job, Collection<Pair<Text,Text>> columnFamilyColumnQualifierPairs) {
     ArgumentChecker.notNull(columnFamilyColumnQualifierPairs);
     ArrayList<String> columnStrings = new ArrayList<String>(columnFamilyColumnQualifierPairs.size());
     for (Pair<Text,Text> column : columnFamilyColumnQualifierPairs) {
         col += ":" + new String(Base64.encodeBase64(TextUtil.getBytes(column.getSecond())));
       columnStrings.add(col);
     }
    job.getConfiguration().setStrings(COLUMNS, columnStrings.toArray(new String[0]));
   }
   
   public static void setLogLevel(JobContext job, Level level) {
     ArgumentChecker.notNull(level);
     log.setLevel(level);
    job.getConfiguration().setInt(LOGLEVEL, level.toInt());
   }
   
   /**
    *          The priority of the iterator
    * @param cfg
    *          The configuration of the iterator
    */
   public static void addIterator(JobContext job, IteratorSetting cfg) {
     // First check to see if anything has been set already
    String iterators = job.getConfiguration().get(ITERATORS);
     
     // No iterators specified yet, create a new string
     if (iterators == null || iterators.isEmpty()) {
       iterators = iterators.concat(ITERATORS_DELIM + new AccumuloIterator(cfg.getPriority(), cfg.getIteratorClass(), cfg.getName()).toString());
     }
     // Store the iterators w/ the job
    job.getConfiguration().set(ITERATORS, iterators);
     for (Entry<String,String> entry : cfg.getProperties().entrySet()) {
       if (entry.getValue() == null)
         continue;
       
      String iteratorOptions = job.getConfiguration().get(ITERATORS_OPTIONS);
       
       // No options specified yet, create a new string
       if (iteratorOptions == null || iteratorOptions.isEmpty()) {
       }
       
       // Store the options w/ the job
      job.getConfiguration().set(ITERATORS_OPTIONS, iteratorOptions);
     }
   }
   
    * @param iteratorName
    *          the iterator name
    * 
   * @deprecated since 1.4, see {@link #addIterator(JobContext, IteratorSetting)}
    */
   public static void setIterator(JobContext job, int priority, String iteratorClass, String iteratorName) {
     // First check to see if anything has been set already
    * @param value
    *          the value
    * 
   * @deprecated since 1.4, see {@link #addIterator(JobContext, IteratorSetting)}
    */
   public static void setIteratorOption(JobContext job, String iteratorName, String key, String value) {
     if (iteratorName == null || key == null || value == null)
     job.getConfiguration().set(ITERATORS_OPTIONS, iteratorOptions);
   }
   
   protected static boolean isIsolated(JobContext job) {
    return job.getConfiguration().getBoolean(ISOLATED, false);
   }
   
   protected static boolean usesLocalIterators(JobContext job) {
    return job.getConfiguration().getBoolean(LOCAL_ITERATORS, false);
   }
   
   protected static String getUsername(JobContext job) {
    return job.getConfiguration().get(USERNAME);
   }
   
   /**
    * WARNING: The password is stored in the Configuration and shared with all MapReduce tasks; It is BASE64 encoded to provide a charset safe conversion to a
    * string, and is not intended to be secure.
    */
   protected static byte[] getPassword(JobContext job) {
    return Base64.decodeBase64(job.getConfiguration().get(PASSWORD, "").getBytes());
   }
   
   protected static String getTablename(JobContext job) {
    return job.getConfiguration().get(TABLE_NAME);
   }
   
   protected static Authorizations getAuthorizations(JobContext job) {
    String authString = job.getConfiguration().get(AUTHORIZATIONS);
     return authString == null ? Constants.NO_AUTHS : new Authorizations(authString.split(","));
   }
   
   protected static Instance getInstance(JobContext job) {
    Configuration conf = job.getConfiguration();
     if (conf.getBoolean(MOCK, false))
       return new MockInstance(conf.get(INSTANCE_NAME));
     return new ZooKeeperInstance(conf.get(INSTANCE_NAME), conf.get(ZOOKEEPERS));
   }
   
   protected static TabletLocator getTabletLocator(JobContext job) throws TableNotFoundException {
    if (job.getConfiguration().getBoolean(MOCK, false))
       return new MockTabletLocator();
    Instance instance = getInstance(job);
    String username = getUsername(job);
    byte[] password = getPassword(job);
    String tableName = getTablename(job);
     return TabletLocator.getInstance(instance, new AuthInfo(username, ByteBuffer.wrap(password), instance.getInstanceID()),
         new Text(Tables.getTableId(instance, tableName)));
   }
   
   protected static List<Range> getRanges(JobContext job) throws IOException {
     ArrayList<Range> ranges = new ArrayList<Range>();
    for (String rangeString : job.getConfiguration().getStringCollection(RANGES)) {
       ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(rangeString.getBytes()));
       Range range = new Range();
       range.readFields(new DataInputStream(bais));
     return ranges;
   }
   
   protected static String getRegex(JobContext job, RegexType type) {
     String key = null;
     switch (type) {
       case ROW:
         throw new NoSuchElementException();
     }
     try {
      String s = job.getConfiguration().get(key);
       if (s == null)
         return null;
       return URLDecoder.decode(s, "UTF-8");
     }
   }
   
   protected static Set<Pair<Text,Text>> getFetchedColumns(JobContext job) {
     Set<Pair<Text,Text>> columns = new HashSet<Pair<Text,Text>>();
    for (String col : job.getConfiguration().getStringCollection(COLUMNS)) {
       int idx = col.indexOf(":");
       Text cf = new Text(idx < 0 ? Base64.decodeBase64(col.getBytes()) : Base64.decodeBase64(col.substring(0, idx).getBytes()));
       Text cq = idx < 0 ? null : new Text(Base64.decodeBase64(col.substring(idx + 1).getBytes()));
     return columns;
   }
   
   protected static boolean getAutoAdjustRanges(JobContext job) {
    return job.getConfiguration().getBoolean(AUTO_ADJUST_RANGES, true);
   }
   
   protected static Level getLogLevel(JobContext job) {
    return Level.toLevel(job.getConfiguration().getInt(LOGLEVEL, Level.INFO.toInt()));
   }
   
   // InputFormat doesn't have the equivalent of OutputFormat's
   // checkOutputSpecs(JobContext job)
   protected static void validateOptions(JobContext job) throws IOException {
    Configuration conf = job.getConfiguration();
     if (!conf.getBoolean(INPUT_INFO_HAS_BEEN_SET, false))
       throw new IOException("Input info has not been set.");
     if (!conf.getBoolean(INSTANCE_HAS_BEEN_SET, false))
       throw new IOException("Instance info has not been set.");
     // validate that we can connect as configured
     try {
      Connector c = getInstance(job).getConnector(getUsername(job), getPassword(job));
      if (!c.securityOperations().authenticateUser(getUsername(job), getPassword(job)))
         throw new IOException("Unable to authenticate user");
      if (!c.securityOperations().hasTablePermission(getUsername(job), getTablename(job), TablePermission.READ))
         throw new IOException("Unable to access table");
       
      if (!usesLocalIterators(job)) {
         // validate that any scan-time iterators can be loaded by the the tablet servers
        for (AccumuloIterator iter : getIterators(job)) {
           if (!c.instanceOperations().testClassLoad(iter.getIteratorClass(), SortedKeyValueIterator.class.getName()))
             throw new AccumuloException("Servers are unable to load " + iter.getIteratorClass() + " as a " + SortedKeyValueIterator.class.getName());
         }
   }
   
   // Get the maxVersions the VersionsIterator should be configured with. Return -1 if none.
   protected static int getMaxVersions(JobContext job) {
    return job.getConfiguration().getInt(MAX_VERSIONS, -1);
   }
   
   // Return a list of the iterator settings (for iterators to apply to a scanner)
   protected static List<AccumuloIterator> getIterators(JobContext job) {
     
    String iterators = job.getConfiguration().get(ITERATORS);
     
     // If no iterators are present, return an empty list
     if (iterators == null || iterators.isEmpty())
       return new ArrayList<AccumuloIterator>();
     
     // Compose the set of iterators encoded in the job configuration
    StringTokenizer tokens = new StringTokenizer(job.getConfiguration().get(ITERATORS), ITERATORS_DELIM);
     List<AccumuloIterator> list = new ArrayList<AccumuloIterator>();
     while (tokens.hasMoreTokens()) {
       String itstring = tokens.nextToken();
   }
   
   // Return a list of the iterator options specified
   protected static List<AccumuloIteratorOption> getIteratorOptions(JobContext job) {
    String iteratorOptions = job.getConfiguration().get(ITERATORS_OPTIONS);
     
     // If no options are present, return an empty list
     if (iteratorOptions == null || iteratorOptions.isEmpty())
       return new ArrayList<AccumuloIteratorOption>();
     
     // Compose the set of options encoded in the job configuration
    StringTokenizer tokens = new StringTokenizer(job.getConfiguration().get(ITERATORS_OPTIONS), ITERATORS_DELIM);
     List<AccumuloIteratorOption> list = new ArrayList<AccumuloIteratorOption>();
     while (tokens.hasMoreTokens()) {
       String optionString = tokens.nextToken();
       }
     }
     
     protected boolean setupRegex(TaskAttemptContext attempt, Scanner scanner) throws AccumuloException {
       try {
        checkAndEnableRegex(getRegex(attempt, RegexType.ROW), scanner, "setRowRegex");
        checkAndEnableRegex(getRegex(attempt, RegexType.COLUMN_FAMILY), scanner, "setColumnFamilyRegex");
        checkAndEnableRegex(getRegex(attempt, RegexType.COLUMN_QUALIFIER), scanner, "setColumnQualifierRegex");
        checkAndEnableRegex(getRegex(attempt, RegexType.VALUE), scanner, "setValueRegex");
         return true;
       } catch (Exception e) {
         throw new AccumuloException("Can't set up regex for scanner");
       }
     }
     
     // Apply the configured iterators from the job to the scanner
     protected void setupIterators(TaskAttemptContext attempt, Scanner scanner) throws AccumuloException {
      List<AccumuloIterator> iterators = getIterators(attempt);
      List<AccumuloIteratorOption> options = getIteratorOptions(attempt);
       
       Map<String,IteratorSetting> scanIterators = new HashMap<String,IteratorSetting>();
       for (AccumuloIterator iterator : iterators) {
     }
     
     // Apply the VersioningIterator at priority 0 based on the job config
     protected void setupMaxVersions(TaskAttemptContext attempt, Scanner scanner) {
      int maxVersions = getMaxVersions(attempt);
       // Check to make sure its a legit value
       if (maxVersions >= 1) {
         IteratorSetting vers = new IteratorSetting(0, "vers", VersioningIterator.class);
       Scanner scanner;
       split = (RangeInputSplit) inSplit;
       log.debug("Initializing input split: " + split.range);
      Instance instance = getInstance(attempt);
      String user = getUsername(attempt);
      byte[] password = getPassword(attempt);
      Authorizations authorizations = getAuthorizations(attempt);
       
       try {
         log.debug("Creating connector with user: " + user);
         Connector conn = instance.getConnector(user, password);
        log.debug("Creating scanner for table: " + getTablename(attempt));
         log.debug("Authorizations are: " + authorizations);
        scanner = conn.createScanner(getTablename(attempt), authorizations);
        if (isIsolated(attempt)) {
           log.info("Creating isolated scanner");
           scanner = new IsolatedScanner(scanner);
         }
        if (usesLocalIterators(attempt)) {
           log.info("Using local iterators");
           scanner = new ClientSideIteratorScanner(scanner);
         }
        setupMaxVersions(attempt, scanner);
        setupRegex(attempt, scanner);
        setupIterators(attempt, scanner);
       } catch (Exception e) {
         throw new IOException(e);
       }
       
       // setup a scanner within the bounds of this split
      for (Pair<Text,Text> c : getFetchedColumns(attempt)) {
         if (c.getSecond() != null) {
           log.debug("Fetching column " + c.getFirst() + ":" + c.getSecond());
           scanner.fetchColumn(c.getFirst(), c.getSecond());
    * read the metadata table to get tablets of interest these each become a split
    */
   public List<InputSplit> getSplits(JobContext job) throws IOException {
    log.setLevel(getLogLevel(job));
    validateOptions(job);
     
    String tableName = getTablename(job);
    boolean autoAdjust = getAutoAdjustRanges(job);
    List<Range> ranges = autoAdjust ? Range.mergeOverlapping(getRanges(job)) : getRanges(job);
     
     if (ranges.isEmpty()) {
       ranges = new ArrayList<Range>(1);
     Map<String,Map<KeyExtent,List<Range>>> binnedRanges = new HashMap<String,Map<KeyExtent,List<Range>>>();
     TabletLocator tl;
     try {
      tl = getTabletLocator(job);
       while (!tl.binRanges(ranges, binnedRanges).isEmpty()) {
         log.warn("Unable to locate bins for specified ranges. Retrying.");
        UtilWaitThread.sleep(100 + (int) (Math.random() * 100)); // sleep
        // randomly
        // between
        // 100
        // and
        // 200
        // ms
       }
     } catch (Exception e) {
       throw new IOException(e);
