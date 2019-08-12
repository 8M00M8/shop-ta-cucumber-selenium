# Shop Test Automation
Cucumber JVM, Selenium WebDriver

## Clean

`gradlew clean`

## Tests Execution

`gradlew cucumber`
* `-Denv=demo` - Environment. Default: (gradle.properties).
* `-Dtarget=grid` - Target
    * local (default)
    * grid
* `-Dbrowser=firefox` - Browser
    * chrome (default)
    * firefox
* `-Dthreads=1` - Parallel Cucumber threads. Default: 1 (gradle.properties).
* `-Dlog.level=ALL` - Log4j 2 logging levels for tests
    * ALL
    * TRACE
    * DEBUG
    * INFO
    * WARN
    * ERROR (default)
    * FATAL
    * OFF

### Tags

* `-Dtags=<Tag_Expression>` - Run tests using [Tag Expressions](https://cucumber.io/docs/cucumber/api/#tag-expressions).  
Tag Expressions are boolean expressions of tags with the logical operators `and`, `or`, `not` and the parentheses `(`, `)`.  
Default: all tests as 'not @ignore' (gradle.properties).  
Tag Expressions:
    * `@tag_name` - Run tests with @tag_name tag;
    * `'not @tag_name'` - Logical NOT;
    * `'@tag_name or @tag_name2'` - Logical OR;
    * `'@tag_name and @tag_name2'` - Logical AND;
    * `'not @tag_name and (@tag_name2 or @tag_name3)'` - Parentheses `(`, `)`;

## Allure Report

`gradlew allureReport` - Creates Allure report for a single-module project.

`gradlew allureServe` - Creates Allure report for a single-module project in the tmp folder and opens it in the default browser.