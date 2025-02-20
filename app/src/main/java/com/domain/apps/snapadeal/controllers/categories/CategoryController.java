package com.domain.apps.snapadeal.controllers.categories;

import com.domain.apps.snapadeal.classes.Category;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Droideve on 7/12/2017.
 */

public class CategoryController {


    public static List<Category> getArrayList() {

        List<Category> results = new ArrayList<>();
        RealmList<Category> listCats = CategoryController.list();

        results.addAll(listCats.subList(0, listCats.size()));
        return results;
    }

    public static RealmList<Category> list() {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Category> result = realm.where(Category.class).findAll();

        RealmList<Category> results = new RealmList<Category>();
        results.addAll(result.subList(0, result.size()));

        return results;
    }

    public static boolean insertCategory(final Category cat) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

//               RealmResults<Category> r = realm.where(Category.class).findAll();
//                r.deleteAllFromRealm();

                realm.copyToRealmOrUpdate(cat);
            }
        });

        return true;
    }

    public static boolean insertCategories(final RealmList<Category> list) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(list);
                /*RealmList<Category> oldList= CategoryController.list();
                if(oldList.size()>0)
                for (int i=0;i<oldList.size();i++){
                    if(!oldList.get(i).isMenu())
                        oldList.get(i).deleteFromRealm();
                }*/
            }
        });

        return true;
    }


    public static Category findId(int id) {

        Realm realm = Realm.getDefaultInstance();
        Category obj = realm.where(Category.class).equalTo("numCat", id).findFirst();

        return obj;
    }

    public static void removeAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Category> result = realm.where(Category.class).findAll();
                for (Category cat : result) {
                    cat.deleteFromRealm();
                }
            }
        });

    }

}
