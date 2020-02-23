package com.ci123.db;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ci123.dbmodule.litepalmannager.DbManager;
import com.ci123.dbmodule.litepalmannager.listener.DbListener;
import com.ci123.dbmodule.litepalmannager.listener.DeleteListener;
import com.ci123.dbmodule.litepalmannager.listener.InsertListener;
import com.ci123.dbmodule.litepalmannager.listener.QueryListener;
import com.ci123.dbmodule.litepalmannager.listener.UpdateListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

    /**
     * 创建数据库
     */
    private Button mCreateDb;
    /**
     * 添加数据
     */
    private Button mAddBtn;
    /**
     * 修改数据
     */
    private Button mUpdateData;
    /**
     * 删除数据
     */
    private Button mDelete;
    /**
     * 查询数据
     */
    private Button mQueryData;
    /**
     * 切换数据库
     */
    private Button mUseDb;
    private ListView mListView;
    private BaseAdapter baseAdapter;
    private List<Book> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DbManager.getInstance().init(getApplicationContext());
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
        initView();
        initData();
    }

    private void initView() {
        mCreateDb = (Button) findViewById(R.id.create_db);
        mCreateDb.setOnClickListener(this);
        mAddBtn = (Button) findViewById(R.id.add_btn);
        mAddBtn.setOnClickListener(this);
        mUpdateData = (Button) findViewById(R.id.update_data);
        mUpdateData.setOnClickListener(this);
        mDelete = (Button) findViewById(R.id.delete);
        mDelete.setOnClickListener(this);
        mQueryData = (Button) findViewById(R.id.query_data);
        mQueryData.setOnClickListener(this);
        mUseDb = (Button) findViewById(R.id.use_db);
        mUseDb.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.listView);
        baseAdapter = new BookAdapter(books);
        mListView.setAdapter(baseAdapter);
        DbManager.getInstance().useNewXmlDb("ceshi");
    }

    private void initData() {
        DbManager.getInstance().getDbQuery().findAllAsync(Book.class, new QueryListener() {
            @Override
            public <T> void Result(final List<T> list) {
                if (list.size() > 0) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            books.clear();
                            books.addAll((Collection<? extends Book>) list);
                            baseAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.create_db:
                cerateDb("ceshi");
                break;
            case R.id.add_btn:
                save();
                break;
            case R.id.update_data:
                update();
                break;
            case R.id.delete:
                delete();
                break;
            case R.id.query_data:
                query();
                break;
            case R.id.use_db:
//                DbManager.getInstance().useNewXmlDb("数据库名");
                break;
        }
    }

    void createNewDb(String dbName) {
        DbManager.getInstance().useNewXmlDb(dbName);
    }

    void cerateDb(final String dbName) {
        EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("创建数据库")
                .setView(editText)
                .setCancelable(false)
                .setPositiveButton("创建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createNewDb(dbName);
                        Book book = new Book();
                        book.setBid(1);
                        book.setBookName("数学");
                        book.setAuthor("张三");
                        book.save();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    void save() {
        View view = getLayoutInflater().inflate(R.layout.save, null);
        final EditText bid_edt = view.findViewById(R.id.bid_edt);
        final EditText booKName_det = view.findViewById(R.id.bookName_edt);
        final EditText bookAuther = view.findViewById(R.id.author_edt);
        new AlertDialog.Builder(this).setCancelable(false)
                .setView(view)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        int bid = 0;
                        if (bid_edt.getText().toString().length() > 0) {
                            bid = Integer.parseInt(bid_edt.getText().toString());
                        }
                        String bookName = booKName_det.getText().toString();
                        String auther = bookAuther.getText().toString();
                        if (bid == 0 || bookName.equals("") || bookAuther.equals("")) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "请输入book完整信息", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Book book = new Book();
                            book.setBookName(bookName);
                            book.setBid(bid);
                            book.setAuthor(auther);
                            book.saveAsync(new InsertListener() {
                                @Override
                                public void onFinsh(boolean isSuccess) {
                                    Toast.makeText(getBaseContext(), "书籍保存" + isSuccess, Toast.LENGTH_SHORT).show();
                                    initData();
                                    dialogInterface.dismiss();
                                }
                            });
                        }


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    void query() {
        View view = getLayoutInflater().inflate(R.layout.save, null);
        final EditText nameEdit = view.findViewById(R.id.bookName_edt);
        final EditText author_edt = view.findViewById(R.id.author_edt);
        view.findViewById(R.id.bid_edt).setVisibility(View.GONE);
        new AlertDialog.Builder(this)
                .setTitle("条件查询")
                .setView(view)
                .setCancelable(false)
                .setNegativeButton("多条件查询", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String bookName = nameEdit.getText().toString();
                        String author = author_edt.getText().toString();
                        if (bookName.isEmpty() || author.isEmpty()) {
                            Toast.makeText(getBaseContext(), "书名和作者名不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            DbManager.getInstance().getDbQuery().where("bookName = ? and author =?", bookName, author)
                                    .findAsync(Book.class, new QueryListener() {
                                        @Override
                                        public <T> void Result(List<T> list) {
                                            books.clear();
                                            books.addAll((Collection<? extends Book>) list);
                                            baseAdapter.notifyDataSetChanged();
                                            if (list.size() == 0) {
                                                Toast.makeText(getBaseContext(), "查无记录", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                }).setPositiveButton("书名查询", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String bookName = nameEdit.getText().toString();
                if (bookName.isEmpty()) {
                    Toast.makeText(getBaseContext(), "书名不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    DbManager.getInstance().getDbQuery().where("bookName = ?", bookName).findAsync(Book.class, new QueryListener() {
                        @Override
                        public <T> void Result(List<T> list) {
                            books.clear();
                            books.addAll((Collection<? extends Book>) list);
                            baseAdapter.notifyDataSetChanged();
                            if (list.size() == 0) {
                                Toast.makeText(getBaseContext(), "查无记录", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private class BookAdapter extends BaseAdapter {
        List<Book> books;

        public BookAdapter(List<Book> books) {
            this.books = books;
        }

        @Override
        public int getCount() {
            return books.size();
        }

        @Override
        public Book getItem(int i) {
            if (books != null) {
                return books.get(i);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item, null);
                viewHolder = new ViewHolder();
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.text = view.findViewById(R.id.text);
            viewHolder.text.setText(getItem(i).toString());
            return view;
        }

        class ViewHolder {
            TextView text;
        }
    }

    void delete() {
        View view = getLayoutInflater().inflate(R.layout.save, null);
        final EditText nameEdit = view.findViewById(R.id.bookName_edt);
        final EditText author_edt = view.findViewById(R.id.author_edt);
        view.findViewById(R.id.bid_edt).setVisibility(View.GONE);
        new AlertDialog.Builder(this)
                .setTitle("删除数据")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("书名条件删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String bookName = nameEdit.getText().toString();
                        if (bookName.isEmpty()) {
                            Toast.makeText(getBaseContext(), "请输入书名", Toast.LENGTH_SHORT).show();
                        } else {
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
                            }, "bookName = ?", bookName);
                        }

                    }
                }).setNegativeButton("作者名删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String author = author_edt.getText().toString();
                if (author.isEmpty()) {
                    Toast.makeText(getBaseContext(), "请输入作者名", Toast.LENGTH_SHORT).show();
                } else {
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
                    }, "author = ?", author);
                }

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    void update() {
        View view = getLayoutInflater().inflate(R.layout.save, null);
        final EditText nameEdit = view.findViewById(R.id.bookName_edt);
        final EditText author_edt = view.findViewById(R.id.author_edt);
        author_edt.setHint("请输入新的作者名");
        view.findViewById(R.id.bid_edt).setVisibility(View.GONE);
        new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .setTitle("更新书的作者")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String oldName = nameEdit.getText().toString();
                        String newName = author_edt.getText().toString();
                        Book book = new Book();
                        book.setAuthor(newName);
                        book.updateAllAsync(new UpdateListener() {
                            @Override
                            public void result(int rowsAffected) {
                                if (rowsAffected > 0) {
                                    Toast.makeText(getBaseContext(), "更新成功", Toast.LENGTH_SHORT).show();
                                    initData();
                                } else {
                                    Toast.makeText(getBaseContext(), "暂无数据更新", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, "bookName= ?", oldName);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();

    }

}
