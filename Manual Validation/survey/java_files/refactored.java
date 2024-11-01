    * 
    * @param job
    * @param enable
   * @deprecated Use {@link #setIsolated(Configuration,boolean)} instead
    */
   public static void setIsolated(JobContext job, boolean enable) {
    setIsolated(job.getConfiguration(), enable);
  }

  /**
   * Enable or disable use of the {@link IsolatedScanner}. By default it is not enabled.
   * 
   * @param conf
   * @param enable
   */
  public static void setIsolated(Configuration conf, boolean enable) {
     conf.setBoolean(ISOLATED, enable);
   }
   
    * 
    * @param job
    * @param enable
   * @deprecated Use {@link #setLocalIterators(Configuration,boolean)} instead
    */
   public static void setLocalIterators(JobContext job, boolean enable) {
    setLocalIterators(job.getConfiguration(), enable);
  }

  /**
   * Enable or disable use of the {@link ClientSideIteratorScanner}. By default it is not enabled.
   * 
   * @param job
   * @param enable
   */
  public static void setLocalIterators(Configuration conf, boolean enable) {
     conf.setBoolean(LOCAL_ITERATORS, enable);
   }
   
  /**
   * @deprecated Use {@link #setInputInfo(Configuration,String,byte[],String,Authorizations)} instead
   */
   public static void setInputInfo(JobContext job, String user, byte[] passwd, String table, Authorizations auths) {
    setInputInfo(job.getConfiguration(), user, passwd, table, auths);
  }

  public static void setInputInfo(Configuration conf, String user, byte[] passwd, String table, Authorizations auths) {
     if (conf.getBoolean(INPUT_INFO_HAS_BEEN_SET, false))
       throw new IllegalStateException("Input info can only be set once per job");
     conf.setBoolean(INPUT_INFO_HAS_BEEN_SET, true);
       conf.set(AUTHORIZATIONS, auths.serialize());
   }
   
  /**
   * @deprecated Use {@link #setZooKeeperInstance(Configuration,String,String)} instead
   */
   public static void setZooKeeperInstance(JobContext job, String instanceName, String zooKeepers) {
    setZooKeeperInstance(job.getConfiguration(), instanceName, zooKeepers);
  }

  public static void setZooKeeperInstance(Configuration conf, String instanceName, String zooKeepers) {
     if (conf.getBoolean(INSTANCE_HAS_BEEN_SET, false))
       throw new IllegalStateException("Instance info can only be set once per job");
     conf.setBoolean(INSTANCE_HAS_BEEN_SET, true);
     conf.set(ZOOKEEPERS, zooKeepers);
   }
   
  /**
   * @deprecated Use {@link #setMockInstance(Configuration,String)} instead
   */
   public static void setMockInstance(JobContext job, String instanceName) {
    setMockInstance(job.getConfiguration(), instanceName);
  }

  public static void setMockInstance(Configuration conf, String instanceName) {
     conf.setBoolean(INSTANCE_HAS_BEEN_SET, true);
     conf.setBoolean(MOCK, true);
     conf.set(INSTANCE_NAME, instanceName);
   }
   
  /**
   * @deprecated Use {@link #setRanges(Configuration,Collection<Range>)} instead
   */
   public static void setRanges(JobContext job, Collection<Range> ranges) {
    setRanges(job.getConfiguration(), ranges);
  }

  public static void setRanges(Configuration conf, Collection<Range> ranges) {
     ArgumentChecker.notNull(ranges);
     ArrayList<String> rangeStrings = new ArrayList<String>(ranges.size());
     try {
     } catch (IOException ex) {
       throw new IllegalArgumentException("Unable to encode ranges to Base64", ex);
     }
    conf.setStrings(RANGES, rangeStrings.toArray(new String[0]));
   }
   
  /**
   * @deprecated Use {@link #disableAutoAdjustRanges(Configuration)} instead
   */
   public static void disableAutoAdjustRanges(JobContext job) {
    disableAutoAdjustRanges(job.getConfiguration());
  }

  public static void disableAutoAdjustRanges(Configuration conf) {
    conf.setBoolean(AUTO_ADJUST_RANGES, false);
   }
   
   public static enum RegexType {
     ROW, COLUMN_FAMILY, COLUMN_QUALIFIER, VALUE
   }
   
   /**
   * @deprecated since 1.4 {@link #addIterator(Configuration, IteratorSetting)}
    * @see org.apache.accumulo.core.iterators.user.RegExFilter#setRegexs(IteratorSetting, String, String, String, String, boolean)
    * @param job
    * @param type
    * @param maxVersions
    *          the max versions
    * @throws IOException
   * @deprecated Use {@link #setMaxVersions(Configuration,int)} instead
    */
   public static void setMaxVersions(JobContext job, int maxVersions) throws IOException {
    setMaxVersions(job.getConfiguration(), maxVersions);
  }

  /**
   * Sets the max # of values that may be returned for an individual Accumulo cell. By default, applied before all other Accumulo iterators (highest priority)
   * leveraged in the scan by the record reader. To adjust priority use setIterator() & setIteratorOptions() w/ the VersioningIterator type explicitly.
   * 
   * @param conf
   *          the job
   * @param maxVersions
   *          the max versions
   * @throws IOException
   */
  public static void setMaxVersions(Configuration conf, int maxVersions) throws IOException {
     if (maxVersions < 1)
       throw new IOException("Invalid maxVersions: " + maxVersions + ".  Must be >= 1");
    conf.setInt(MAX_VERSIONS, maxVersions);
   }
   
   /**
    * 
    * @param columnFamilyColumnQualifierPairs
    *          A pair of {@link Text} objects corresponding to column family and column qualifier. If the column qualifier is null, the entire column family is
    *          selected. An empty set is the default and is equivalent to scanning the all columns.
   * @deprecated Use {@link #fetchColumns(Configuration,Collection<Pair<Text, Text>>)} instead
    */
   public static void fetchColumns(JobContext job, Collection<Pair<Text,Text>> columnFamilyColumnQualifierPairs) {
    fetchColumns(job.getConfiguration(), columnFamilyColumnQualifierPairs);
  }
  
  /**
   * 
   * @param columnFamilyColumnQualifierPairs
   *          A pair of {@link Text} objects corresponding to column family and column qualifier. If the column qualifier is null, the entire column family is
   *          selected. An empty set is the default and is equivalent to scanning the all columns.
   */
  public static void fetchColumns(Configuration conf, Collection<Pair<Text,Text>> columnFamilyColumnQualifierPairs) {
     ArgumentChecker.notNull(columnFamilyColumnQualifierPairs);
     ArrayList<String> columnStrings = new ArrayList<String>(columnFamilyColumnQualifierPairs.size());
     for (Pair<Text,Text> column : columnFamilyColumnQualifierPairs) {
         col += ":" + new String(Base64.encodeBase64(TextUtil.getBytes(column.getSecond())));
       columnStrings.add(col);
     }
    conf.setStrings(COLUMNS, columnStrings.toArray(new String[0]));
   }
   
  /**
   * @deprecated Use {@link #setLogLevel(Configuration,Level)} instead
   */
   public static void setLogLevel(JobContext job, Level level) {
    setLogLevel(job.getConfiguration(), level);
  }
  
  public static void setLogLevel(Configuration conf, Level level) {
     ArgumentChecker.notNull(level);
     log.setLevel(level);
    conf.setInt(LOGLEVEL, level.toInt());
   }
   
   /**
    *          The priority of the iterator
    * @param cfg
    *          The configuration of the iterator
   * @deprecated Use {@link #addIterator(Configuration,IteratorSetting)} instead
    */
   public static void addIterator(JobContext job, IteratorSetting cfg) {
    addIterator(job.getConfiguration(), cfg);
  }

  /**
   * Encode an iterator on the input.
   * 
   * @param conf
   *          The job in which to save the iterator configuration
   * @param priority
   *          The priority of the iterator
   * @param cfg
   *          The configuration of the iterator
   */
  public static void addIterator(Configuration conf, IteratorSetting cfg) {
     // First check to see if anything has been set already
    String iterators = conf.get(ITERATORS);
     
     // No iterators specified yet, create a new string
     if (iterators == null || iterators.isEmpty()) {
       iterators = iterators.concat(ITERATORS_DELIM + new AccumuloIterator(cfg.getPriority(), cfg.getIteratorClass(), cfg.getName()).toString());
     }
     // Store the iterators w/ the job
    conf.set(ITERATORS, iterators);
     for (Entry<String,String> entry : cfg.getProperties().entrySet()) {
       if (entry.getValue() == null)
         continue;
       
      String iteratorOptions = conf.get(ITERATORS_OPTIONS);
       
       // No options specified yet, create a new string
       if (iteratorOptions == null || iteratorOptions.isEmpty()) {
       }
       
       // Store the options w/ the job
      conf.set(ITERATORS_OPTIONS, iteratorOptions);
     }
   }
   
    * @param iteratorName
    *          the iterator name
    * 
   * @deprecated since 1.4, see {@link #addIterator(Configuration, IteratorSetting)}
    */
   public static void setIterator(JobContext job, int priority, String iteratorClass, String iteratorName) {
     // First check to see if anything has been set already
    * @param value
    *          the value
    * 
   * @deprecated since 1.4, see {@link #addIterator(Configuration, IteratorSetting)}
    */
   public static void setIteratorOption(JobContext job, String iteratorName, String key, String value) {
     if (iteratorName == null || key == null || value == null)
     job.getConfiguration().set(ITERATORS_OPTIONS, iteratorOptions);
   }
   
  /**
   * @deprecated Use {@link #isIsolated(Configuration)} instead
   */
   protected static boolean isIsolated(JobContext job) {
    return isIsolated(job.getConfiguration());
  }

  protected static boolean isIsolated(Configuration conf) {
    return conf.getBoolean(ISOLATED, false);
   }
   
  /**
   * @deprecated Use {@link #usesLocalIterators(Configuration)} instead
   */
   protected static boolean usesLocalIterators(JobContext job) {
    return usesLocalIterators(job.getConfiguration());
  }

  protected static boolean usesLocalIterators(Configuration conf) {
    return conf.getBoolean(LOCAL_ITERATORS, false);
   }
   
  /**
   * @deprecated Use {@link #getUsername(Configuration)} instead
   */
   protected static String getUsername(JobContext job) {
    return getUsername(job.getConfiguration());
  }

  protected static String getUsername(Configuration conf) {
    return conf.get(USERNAME);
   }
   
   /**
    * WARNING: The password is stored in the Configuration and shared with all MapReduce tasks; It is BASE64 encoded to provide a charset safe conversion to a
    * string, and is not intended to be secure.
   * @deprecated Use {@link #getPassword(Configuration)} instead
    */
   protected static byte[] getPassword(JobContext job) {
    return getPassword(job.getConfiguration());
  }

  /**
   * WARNING: The password is stored in the Configuration and shared with all MapReduce tasks; It is BASE64 encoded to provide a charset safe conversion to a
   * string, and is not intended to be secure.
   */
  protected static byte[] getPassword(Configuration conf) {
    return Base64.decodeBase64(conf.get(PASSWORD, "").getBytes());
   }
   
  /**
   * @deprecated Use {@link #getTablename(Configuration)} instead
   */
   protected static String getTablename(JobContext job) {
    return getTablename(job.getConfiguration());
  }

  protected static String getTablename(Configuration conf) {
    return conf.get(TABLE_NAME);
   }
   
  /**
   * @deprecated Use {@link #getAuthorizations(Configuration)} instead
   */
   protected static Authorizations getAuthorizations(JobContext job) {
    return getAuthorizations(job.getConfiguration());
  }

  protected static Authorizations getAuthorizations(Configuration conf) {
    String authString = conf.get(AUTHORIZATIONS);
     return authString == null ? Constants.NO_AUTHS : new Authorizations(authString.split(","));
   }
   
  /**
   * @deprecated Use {@link #getInstance(Configuration)} instead
   */
   protected static Instance getInstance(JobContext job) {
    return getInstance(job.getConfiguration());
  }

  protected static Instance getInstance(Configuration conf) {
     if (conf.getBoolean(MOCK, false))
       return new MockInstance(conf.get(INSTANCE_NAME));
     return new ZooKeeperInstance(conf.get(INSTANCE_NAME), conf.get(ZOOKEEPERS));
   }
   
  /**
   * @deprecated Use {@link #getTabletLocator(Configuration)} instead
   */
   protected static TabletLocator getTabletLocator(JobContext job) throws TableNotFoundException {
    return getTabletLocator(job.getConfiguration());
  }

  protected static TabletLocator getTabletLocator(Configuration conf) throws TableNotFoundException {
    if (conf.getBoolean(MOCK, false))
       return new MockTabletLocator();
    Instance instance = getInstance(conf);
    String username = getUsername(conf);
    byte[] password = getPassword(conf);
    String tableName = getTablename(conf);
     return TabletLocator.getInstance(instance, new AuthInfo(username, ByteBuffer.wrap(password), instance.getInstanceID()),
         new Text(Tables.getTableId(instance, tableName)));
   }
   
  /**
   * @deprecated Use {@link #getRanges(Configuration)} instead
   */
   protected static List<Range> getRanges(JobContext job) throws IOException {
    return getRanges(job.getConfiguration());
  }

  protected static List<Range> getRanges(Configuration conf) throws IOException {
     ArrayList<Range> ranges = new ArrayList<Range>();
    for (String rangeString : conf.getStringCollection(RANGES)) {
       ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(rangeString.getBytes()));
       Range range = new Range();
       range.readFields(new DataInputStream(bais));
     return ranges;
   }
   
  /**
   * @deprecated Use {@link #getRegex(Configuration,RegexType)} instead
   */
   protected static String getRegex(JobContext job, RegexType type) {
    return getRegex(job.getConfiguration(), type);
  }

  protected static String getRegex(Configuration conf, RegexType type) {
     String key = null;
     switch (type) {
       case ROW:
         throw new NoSuchElementException();
     }
     try {
      String s = conf.get(key);
       if (s == null)
         return null;
       return URLDecoder.decode(s, "UTF-8");
     }
   }
   
  /**
   * @deprecated Use {@link #getFetchedColumns(Configuration)} instead
   */
   protected static Set<Pair<Text,Text>> getFetchedColumns(JobContext job) {
    return getFetchedColumns(job.getConfiguration());
  }

  protected static Set<Pair<Text,Text>> getFetchedColumns(Configuration conf) {
     Set<Pair<Text,Text>> columns = new HashSet<Pair<Text,Text>>();
    for (String col : conf.getStringCollection(COLUMNS)) {
       int idx = col.indexOf(":");
       Text cf = new Text(idx < 0 ? Base64.decodeBase64(col.getBytes()) : Base64.decodeBase64(col.substring(0, idx).getBytes()));
       Text cq = idx < 0 ? null : new Text(Base64.decodeBase64(col.substring(idx + 1).getBytes()));
     return columns;
   }
   
  /**
   * @deprecated Use {@link #getAutoAdjustRanges(Configuration)} instead
   */
   protected static boolean getAutoAdjustRanges(JobContext job) {
    return getAutoAdjustRanges(job.getConfiguration());
  }

  protected static boolean getAutoAdjustRanges(Configuration conf) {
    return conf.getBoolean(AUTO_ADJUST_RANGES, true);
   }
   
  /**
   * @deprecated Use {@link #getLogLevel(Configuration)} instead
   */
   protected static Level getLogLevel(JobContext job) {
    return getLogLevel(job.getConfiguration());
  }

  protected static Level getLogLevel(Configuration conf) {
    return Level.toLevel(conf.getInt(LOGLEVEL, Level.INFO.toInt()));
   }
   
   // InputFormat doesn't have the equivalent of OutputFormat's
   // checkOutputSpecs(JobContext job)
  /**
   * @deprecated Use {@link #validateOptions(Configuration)} instead
   */
   protected static void validateOptions(JobContext job) throws IOException {
    validateOptions(job.getConfiguration());
  }

  // InputFormat doesn't have the equivalent of OutputFormat's
  // checkOutputSpecs(JobContext job)
  protected static void validateOptions(Configuration conf) throws IOException {
     if (!conf.getBoolean(INPUT_INFO_HAS_BEEN_SET, false))
       throw new IOException("Input info has not been set.");
     if (!conf.getBoolean(INSTANCE_HAS_BEEN_SET, false))
       throw new IOException("Instance info has not been set.");
     // validate that we can connect as configured
     try {
      Connector c = getInstance(conf).getConnector(getUsername(conf), getPassword(conf));
      if (!c.securityOperations().authenticateUser(getUsername(conf), getPassword(conf)))
         throw new IOException("Unable to authenticate user");
      if (!c.securityOperations().hasTablePermission(getUsername(conf), getTablename(conf), TablePermission.READ))
         throw new IOException("Unable to access table");
       
      if (!usesLocalIterators(conf)) {
         // validate that any scan-time iterators can be loaded by the the tablet servers
        for (AccumuloIterator iter : getIterators(conf)) {
           if (!c.instanceOperations().testClassLoad(iter.getIteratorClass(), SortedKeyValueIterator.class.getName()))
             throw new AccumuloException("Servers are unable to load " + iter.getIteratorClass() + " as a " + SortedKeyValueIterator.class.getName());
         }
   }
   
   // Get the maxVersions the VersionsIterator should be configured with. Return -1 if none.
  /**
   * @deprecated Use {@link #getMaxVersions(Configuration)} instead
   */
   protected static int getMaxVersions(JobContext job) {
    return getMaxVersions(job.getConfiguration());
  }

  // Get the maxVersions the VersionsIterator should be configured with. Return -1 if none.
  protected static int getMaxVersions(Configuration conf) {
    return conf.getInt(MAX_VERSIONS, -1);
   }
   
   // Return a list of the iterator settings (for iterators to apply to a scanner)
  /**
   * @deprecated Use {@link #getIterators(Configuration)} instead
   */
   protected static List<AccumuloIterator> getIterators(JobContext job) {
    return getIterators(job.getConfiguration());
  }

  // Return a list of the iterator settings (for iterators to apply to a scanner)
  protected static List<AccumuloIterator> getIterators(Configuration conf) {
     
    String iterators = conf.get(ITERATORS);
     
     // If no iterators are present, return an empty list
     if (iterators == null || iterators.isEmpty())
       return new ArrayList<AccumuloIterator>();
     
     // Compose the set of iterators encoded in the job configuration
    StringTokenizer tokens = new StringTokenizer(conf.get(ITERATORS), ITERATORS_DELIM);
     List<AccumuloIterator> list = new ArrayList<AccumuloIterator>();
     while (tokens.hasMoreTokens()) {
       String itstring = tokens.nextToken();
   }
   
   // Return a list of the iterator options specified
  /**
   * @deprecated Use {@link #getIteratorOptions(Configuration)} instead
   */
   protected static List<AccumuloIteratorOption> getIteratorOptions(JobContext job) {
    return getIteratorOptions(job.getConfiguration());
  }

  // Return a list of the iterator options specified
  protected static List<AccumuloIteratorOption> getIteratorOptions(Configuration conf) {
    String iteratorOptions = conf.get(ITERATORS_OPTIONS);
     
     // If no options are present, return an empty list
     if (iteratorOptions == null || iteratorOptions.isEmpty())
       return new ArrayList<AccumuloIteratorOption>();
     
     // Compose the set of options encoded in the job configuration
    StringTokenizer tokens = new StringTokenizer(conf.get(ITERATORS_OPTIONS), ITERATORS_DELIM);
     List<AccumuloIteratorOption> list = new ArrayList<AccumuloIteratorOption>();
     while (tokens.hasMoreTokens()) {
       String optionString = tokens.nextToken();
       }
     }
     
    /**
     * @deprecated Use {@link #setupRegex(Configuration,Scanner)} instead
     */
     protected boolean setupRegex(TaskAttemptContext attempt, Scanner scanner) throws AccumuloException {
      return setupRegex(attempt.getConfiguration(), scanner);
    }

    protected boolean setupRegex(Configuration conf, Scanner scanner) throws AccumuloException {
       try {
        checkAndEnableRegex(getRegex(conf, RegexType.ROW), scanner, "setRowRegex");
        checkAndEnableRegex(getRegex(conf, RegexType.COLUMN_FAMILY), scanner, "setColumnFamilyRegex");
        checkAndEnableRegex(getRegex(conf, RegexType.COLUMN_QUALIFIER), scanner, "setColumnQualifierRegex");
        checkAndEnableRegex(getRegex(conf, RegexType.VALUE), scanner, "setValueRegex");
         return true;
       } catch (Exception e) {
         throw new AccumuloException("Can't set up regex for scanner");
       }
     }
     
     // Apply the configured iterators from the job to the scanner
    /**
     * @deprecated Use {@link #setupIterators(Configuration,Scanner)} instead
     */
     protected void setupIterators(TaskAttemptContext attempt, Scanner scanner) throws AccumuloException {
      setupIterators(attempt.getConfiguration(), scanner);
    }

    // Apply the configured iterators from the job to the scanner
    protected void setupIterators(Configuration conf, Scanner scanner) throws AccumuloException {
      List<AccumuloIterator> iterators = getIterators(conf);
      List<AccumuloIteratorOption> options = getIteratorOptions(conf);
       
       Map<String,IteratorSetting> scanIterators = new HashMap<String,IteratorSetting>();
       for (AccumuloIterator iterator : iterators) {
     }
     
     // Apply the VersioningIterator at priority 0 based on the job config
    /**
     * @deprecated Use {@link #setupMaxVersions(Configuration,Scanner)} instead
     */
     protected void setupMaxVersions(TaskAttemptContext attempt, Scanner scanner) {
      setupMaxVersions(attempt.getConfiguration(), scanner);
    }

    // Apply the VersioningIterator at priority 0 based on the job config
    protected void setupMaxVersions(Configuration conf, Scanner scanner) {
      int maxVersions = getMaxVersions(conf);
       // Check to make sure its a legit value
       if (maxVersions >= 1) {
         IteratorSetting vers = new IteratorSetting(0, "vers", VersioningIterator.class);
       Scanner scanner;
       split = (RangeInputSplit) inSplit;
       log.debug("Initializing input split: " + split.range);
      Instance instance = getInstance(attempt.getConfiguration());
      String user = getUsername(attempt.getConfiguration());
      byte[] password = getPassword(attempt.getConfiguration());
      Authorizations authorizations = getAuthorizations(attempt.getConfiguration());
       
       try {
         log.debug("Creating connector with user: " + user);
         Connector conn = instance.getConnector(user, password);
        log.debug("Creating scanner for table: " + getTablename(attempt.getConfiguration()));
         log.debug("Authorizations are: " + authorizations);
        scanner = conn.createScanner(getTablename(attempt.getConfiguration()), authorizations);
        if (isIsolated(attempt.getConfiguration())) {
           log.info("Creating isolated scanner");
           scanner = new IsolatedScanner(scanner);
         }
        if (usesLocalIterators(attempt.getConfiguration())) {
           log.info("Using local iterators");
           scanner = new ClientSideIteratorScanner(scanner);
         }
        setupMaxVersions(attempt.getConfiguration(), scanner);
        setupRegex(attempt.getConfiguration(), scanner);
        setupIterators(attempt.getConfiguration(), scanner);
       } catch (Exception e) {
         throw new IOException(e);
       }
       
       // setup a scanner within the bounds of this split
      for (Pair<Text,Text> c : getFetchedColumns(attempt.getConfiguration())) {
         if (c.getSecond() != null) {
           log.debug("Fetching column " + c.getFirst() + ":" + c.getSecond());
           scanner.fetchColumn(c.getFirst(), c.getSecond());
    * read the metadata table to get tablets of interest these each become a split
    */
   public List<InputSplit> getSplits(JobContext job) throws IOException {
    log.setLevel(getLogLevel(job.getConfiguration()));
    validateOptions(job.getConfiguration());
     
    String tableName = getTablename(job.getConfiguration());
    boolean autoAdjust = getAutoAdjustRanges(job.getConfiguration());
    List<Range> ranges = autoAdjust ? Range.mergeOverlapping(getRanges(job.getConfiguration())) : getRanges(job.getConfiguration());
     
     if (ranges.isEmpty()) {
       ranges = new ArrayList<Range>(1);
     Map<String,Map<KeyExtent,List<Range>>> binnedRanges = new HashMap<String,Map<KeyExtent,List<Range>>>();
     TabletLocator tl;
     try {
      tl = getTabletLocator(job.getConfiguration());
       while (!tl.binRanges(ranges, binnedRanges).isEmpty()) {
         log.warn("Unable to locate bins for specified ranges. Retrying.");
        UtilWaitThread.sleep(100 + (int) (Math.random() * 100)); // sleep randomly between 100 and 200 ms
       }
     } catch (Exception e) {
       throw new IOException(e);
