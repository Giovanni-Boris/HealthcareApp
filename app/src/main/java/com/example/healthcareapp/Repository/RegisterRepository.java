package com.example.healthcareapp.Repository;


import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.Room.RegisterDAO;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class RegisterRepository {

    private final RegisterDAO registerDAO;

    public RegisterRepository(RegisterDAO registerDAO) {
        this.registerDAO = registerDAO;
    }

    public Completable insertRegisters(List<Register> registers) {
        return Completable.fromAction(() -> registerDAO.insertAll(registers))
                .subscribeOn(Schedulers.io());
    }
    public Flowable<List<Register>> getAllRegisters() {
        return Flowable.fromCallable(() -> registerDAO.readAllData())
                .subscribeOn(Schedulers.io());
    }

    public Single<Integer> syncWithFirebase(List<Register> firebaseRegisters) {
        return Single.fromCallable(() -> {
            List<Register> roomRegisters = registerDAO.readAllData();
            List<Register> missingRegisters = findMissingRegisters(roomRegisters, firebaseRegisters);
            registerDAO.insertAll(missingRegisters);
            return missingRegisters.size();
        }).subscribeOn(Schedulers.io());
    }
    private List<Register> findMissingRegisters(List<Register> roomRegisters, List<Register> firebaseRegisters) {
        List<Register> missingRegisters = new ArrayList<>();
        int diff  = firebaseRegisters.size() - roomRegisters.size();
        for(int i=0; i < diff ;i++){
            missingRegisters.add(firebaseRegisters.get(roomRegisters.size() + i));
        }
        return missingRegisters;
    }
}
