# 一、接入方式
## 1、在项目根build.gradle中加入远程仓地址https://maven.oneitfarm.com/content/repositories/releases/
示例：

```
allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
        maven { url "https://maven.oneitfarm.com/content/repositories/releases/" }

    }
}
```
## 2、在项目module中引入远程依赖
```
api 'com.ci123.library:db:0.0.1'
```
## 3、配置 litepal.xml
 在项目的assets文件夹中创建一个名为litepal.xml，然后拷贝下面的代码内容

```
<?xml version="1.0" encoding="utf-8"?>
<litepal>
    <!--
       Define the database name of your application.
       By default each database name should be end with .db.
       If you didn't name your database end with .db,
       LitePal would plus the suffix automatically for you.
       For example:
       <dbname value="demo" />
    -->
    <dbname value="bookdb" />

    <!--
       Define the version of your database. Each time you want
       to upgrade your database, the version tag would helps.
       Modify the models you defined in the mapping tag, and just
       make the version value plus one, the upgrade of database
       will be processed automatically without concern.
         For example:
       <version value="1" />
    -->
    <version value="2" />

    <!--
       Define your models in the list with mapping tag, LitePal will
       create tables for each mapping class. The supported fields
       defined in models will be mapped into columns.
       For example:
       <list>
          <mapping class="com.test.model.Reader" />
          <mapping class="com.test.model.Magazine" />
       </list>
    -->
    <list>
            <mapping class="com.ci123.db.Book"/>
    </list>

    <!--
        Define where the .db file should be. "internal" means the .db file
        will be stored in the database folder of internal storage which no
        one can access. "external" means the .db file will be stored in the
        path to the directory on the primary external storage device where
        the application can place persistent files it owns which everyone
        can access. "internal" will act as default.
        For example:
        <storage value="external" />
    -->
    <storage value="external"/>

</litepal>
```
配置文件说明：
* dbname:配置项目的数据库名称。
* version:配置数据库的版本。每次您要升级数据库时，请在此处加上值。
* list : 配置映射类定义的数据库中的表。
* storage配置应将数据库文件存储在何处。external（外部）和internal（内部）是唯一有效的选项。
## 4、初始化
确保尽早调用此方法。在Application 的onCreate（）方法中就可以了。并始终记住将应用程序上下文用作参数。不要将活动或服务的任何实例用作参数，否则可能会发生内存泄漏。

```
DbManager.getInstance().init(getApplicationContext());
```
# 二、使用方式
## 1、创建表
首先定义模型，例如：您有一个模型Book,这个模型可以定义如下(必须继承DbSupport类)：

```
public class Book extends DbSupport {
    @Column（nullable = false）
    int bid;
    String bookName;
    String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bid=" + bid +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
```
然后将这些modle添加到litepal.xml的映射列表中：
```
   <list>
            <mapping class="com.ci123.db.Book"/>
    </list
```
这些表将在您下次操作数据库时生成。
## 2、升级表
```
public class Book extends DbSupport {
    @Column（nullable = false）
    int bid;
    String bookName;

    public int getBid() {
        return bid;
    }


    public void setBid(int bid) {
        this.bid = bid;
    }


    public String getBookName() {
        return bookName;
    }


    public void setBookName(String bookName) {
        this.bookName = bookName;
    }


    @Override
    public String toString() {
        return "Book{" +
                "bid=" + bid +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                '}'
```
db中的升级表非常容易，只需更具需要修改模型删除book模型中的author属性，然后增加litepal.xml中的版本号。
```
<version value="3" />
```
这些表将在下次操作数据库时进行升级，增加新增的列，并删除原始autor列,除删除的哪些列外，书表中的所有数据都将保留
但是有有以下Db无法处理的升级条件，并将升级表中所有的数据都将清除：

* 添加一个注释为的字段unique = true。
* 将字段的注释更改为unique = true。
* 将字段的注释更改为nullable = false。

上述情况回导致数据丢失

## 3、保存数据
保存数据面向对象，从DbSupport 继承的每个模型都可以使用save方法

（1）同步方式：

```
Book book = new Book();
book.setBid(1);
book.setBookName("数学");
book.setAuthor("张三");
book.save();
```
（2）异步方式
```
Book book = new Book();
book.setBookName(bookName);
book.setBid(bid);
book.setAuthor(auther);
book.saveAsync(new InsertListener() {
    @Override
    public void onFinsh(boolean isSuccess) {

    }
});
```
(3)批量保存
```
List<Book> list = new ArraryList<>();
/**同步保存*/
DbManager.getInstance().getInsert().saveAll(list);
/**异步保存*/
DbManager.getInstance().getInsert().saveAllAsync(list, new InsertListener(){
 @Override
  public void onFinsh(boolean isSuccess) {

}

});
```
## 4、更新数据
更新数据面向对象，从DbSupport 继承的每个墨香都可以使用updateAllAsync（）和updateAll()方法

```
/**
 * 异步修改数据
 *
 * @param listener
 * @param conditions 条件例：("bid= ? and author =?","1","张三"),
 */
public void updateAllAsync(final UpdateListener listener, String... conditions) {

}
/**
*同步修改
*@parama conditions  例：("bid= ? and author =?","1","张三"),
*/
public int updateAll(String... conditions) {}
```
将书作者为张三的书籍的作者都改为作者为李四示例：
异步方式：

```
//异步方式：
Book book = new Book();
book.setAuthor("李四");
book.updateAllAsync(new UpdateListener() {
    @Override
    public void result(int rowsAffected) {
        if (rowsAffected > 0) {
            Toast.makeText(getBaseContext(), "更新成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "暂无数据更新", Toast.LENGTH_SHORT).show();
        }
    }
}, "bookName= ?", 张三);
```
同步方式：
```
//异步方式：
Book book = new Book();
book.setAuthor("李四");
book.updateAll("bookName= ?", 张三)
```
## 5、删除数据(DbDelete)
### 1、删除表中所有数据
```
/**同步方式*/
DbManager.getInstance().getDelete().deleteAll(Book.class)；
/**异步方式*/
DbManager.getInstance().getDelete().deleteAllAsync(Book.class, new DeleteListener() {
    @Override
    public void result(int value) {

    }
});
```
### 2、条件删除
示例:异步删除书名数学的书。

```
/**异步方式*/
DbManager.getInstance().getDelete().deleteByConditionsAsync(Book.class, new DeleteListener() {
    @Override
    public void result(int value) {
        if (value > 0) {
            Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
            initData();
        } else {
            Toast.makeText(getBaseContext(), "删除失败", Toast.LENGTH_SHORT).show();
        }
    }
}, "bookName = ?", "数学");
/**同步方式*/
DbManager.getInstance().getDelete().deleteByConditionsAsync(Book.class, "bookName = ?", "数学");
```
## 6、查询数据
### (1)查询表中所有数据
示例：查询书表中所有书

```
/**同步查询*/
List<Book.class> list = DbManager.getInstance().getDbQuery().findAll(Book.class)；
/**异步查询*/
DbManager.getInstance().getDbQuery().findAllAsync(Book.class, new QueryListener() {
    @Override
    public <T> void Result(final List<T> list) {

    }
});
```
### (2)条件查询
示例：查询书名为数学和作者为张三的书

```
/**异步查询*/
DbManager.getInstance().getDbQuery().where("bookName = ? and author =?", "数学", "张三")
        .findAsync(Book.class, new QueryListener() {
            @Override
            public <T> void Result(List<T> list) {

                }
            }
        });
/**同步查询*/
DbManager.getInstance().getDbQuery().where("bookName = ? and author =?", "数学", "张三").find(Book.class);
```
### (3)使用order、limit、offset连缀查询（可选）
示例：查询bid小于3且作者为张三的书，并按照书名排序

```
/**异步方式*/
DbManager.getInstance().getDbQuery().where("bid < ? and author =?", "3", "张三").order("bookName").findAsync(Book.class, new QueryListener() {
            @Override
            public <T> void Result(List<T> list) {

                }
            }
        });

/**同步方式*/
List<Book.class> list = DbManager.getInstance().getDbQuery().where("bid < ? and author =?", "3", "张三").order("bookName").find（Book.class）;
```
## 7、多个数据库
如果宁需要多个数据库，可以创建多个数据库，并切换使用

### （1）创建一个更litepal.xml一样配置的数据库（默认使用）
说明：如果想创建一个和litepal一样配置的数据库使用此方法，*如果该名数据库已存在则不重新创建，如果表结构有改变则升级，否则就创建并使用此库*

```
DbManager.getInstance().useNewXmlDb("数据库名");
```
### （2）创建新数据库并自定义表
示例：创建一个新的数据库，并创建book表和book1表

```
DbManager.getInstance().useNewDb("数据库名", "版本号", Book.class.getName(), BooK1.class.getName());
```
提示：数据库中的model表定义的model都需要继承DbSupport;
### （3）使用默认数据库
说明：该数据库时litepal.xml定义的数据库

```
DbManager.getInstance().useDefaultDb();
```
### （4）删除数据库
```
DbManager""().getInstance().deleteDb("数据库名");
```
### （5）监听数据库的创建和更新
```
DbManager.getInstance().registDbListener(new DbListener() {
    @Override
    public void onCreate() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "创建一个新的数据库", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUpgrade(int oldVersion, int newVersion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "更新数据库", Toast.LENGTH_SHORT).show();
            }
        });
    }
});
```
# 三、api方法说明：
## 1、DbManager
```
 /**
 * 初始化
 * @param context 应用的context
 * */
public void init(Context context) {

}

public DbQuery getDbQuery() {

}

public DbDelete getDelete() {

}

public DbInsert getInsert() {return DbInsert.getInstance();
}

/**
 * 创建数据库并自定义表
 * @param dbName 数据库名
 * @param version 数据库版本
 * @param modelClassNames model类名
 */
public void useNewDb(String dbName, int version, String... modelClassNames) {

}

/**
 * 创建私用litePal.xml一样配置的数据库,并使用
 *  如果该名数据库已存在则不重新创建，如果表结构有改变则升级，否则就创建并使用此库
 * @param dbName 数据库名
 */
public void useNewXmlDb(String dbName) {

}

/**
 * 使用默认数据库（litepal.xml定义的数据库也就是默认数据库）
 */
public void useDefaultDb() {

}

/**
 * 删除数据库
 * @param dbName 数据库名
 */
public void deleteDb(String dbName) {}


/**
 * 数据库创建更新检测方法
 * @param listener
 */
public void registDbListener(final DbListener listener) {
  /**
 * 同步查询所有表
 * @param modelClass model类
 */
<T> List<T> findAll(Class<T> modelClass);

/**
 * 异步查询所有表数据
 * @param modelClass model类
 * @param listener 查询监听
 */
<T> void findAllAsync(Class<T> modelClass, QueryListener listener);

/**
 * 查找
 * @param s model类
 */
<T> List<T> find(Class<T> s);

/**
 * 异步查找
 * @param modelClass model类
 * @param listener 查询监听
 */
<T> void findAsync(final Class<T> modelClass, QueryListener listener);

/**
 * 排序
 * @param orderBy 表中排序字段
 */
QueryService order(String orderBy);

/**
 * 每次查询结果条数
 * @param limit 查询限制条数
 */
QueryService limit(int limit);

/**
 * 偏移量
 * @param offset 偏移量
 */
QueryService offset(int offset);


/**
 * 查询条件
 * @param conditions 示例（“bookName = ? and bid = ?”, "数学"，“1” ）
 * */
QueryService where(String... conditions);
}
```
## 2、DbQuery
```
/**
 * 同步查询所有表
 * @param modelClass model类
 */
<T> List<T> findAll(Class<T> modelClass);

/**
 * 异步查询所有表数据
 * @param modelClass model类
 * @param listener 查询监听
 */
<T> void findAllAsync(Class<T> modelClass, QueryListener listener);

/**
 * 查找
 * @param s model类
 */
<T> List<T> find(Class<T> s);

/**
 * 异步查找
 * @param modelClass model类
 * @param listener 查询监听
 */
<T> void findAsync(final Class<T> modelClass, QueryListener listener);

/**
 * 排序
 * @param orderBy 表中排序字段
 */
QueryService order(String orderBy);

/**
 * 每次查询结果条数
 * @param limit 查询限制条数
 */
QueryService limit(int limit);

/**
 * 偏移量
 * @param offset 偏移量
 */
QueryService offset(int offset);


/**
 * 查询条件
 * @param conditions 示例（“bookName = ? and bid = ?”, "数学"，“1” ）
 * */
QueryService where(String... conditions);
```
## 3、BbInsert
```
/**
 * 保存数据（同步）
 * @param list model数据集合
 */
<T extends DbSupport> void saveAll(List<T> list);

/**
 * 保存数据（异步）
 * @param list model数据集合
 * @param listener 保存监听
 */
```
<T extends DbSupport> void saveAllAsync(List<T> list, InsertListener listener);


## 4、DbDetete
```
/**
 * 删除表中所有数据（同步）
 *
 * @param modelClass model类
 */
int deleteAll(Class<?> modelClass);

/**
 * 删除表中所有数据（异步）
 *
 * @param modelClass model类
 * @param listener   状态监听类
 */
void deleteAllAsync(Class<?> modelClass, DeleteListener listener);

/**
 * 条件删除（同步）
 *
 * @param modelClass model类
 * @param conditions sql语句条件（“bookName  =? and bid = ?”, "1", "数学"）
 */
int deleteByConditions(Class<?> modelClass, String... conditions);

/**
 * 条件删除（异步）
 *
 * @param modelClass model类
 * @param conditions sql语句条件（“bookName  =? and bid = ?”, "1", "数学"）
 * @param listener 删除监听类
 */
void deleteByConditionsAsync(Class<?> modelClass, DeleteListener listener, String... conditions);
```

