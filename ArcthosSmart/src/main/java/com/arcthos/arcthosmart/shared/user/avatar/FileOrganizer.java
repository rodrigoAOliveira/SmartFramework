package com.arcthos.arcthosmart.shared.user.avatar;

/**
 * ************************************************************
 * Autor : Carolina Oliveira
 * Data : 28/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
public abstract class FileOrganizer {
    public void execute() {
        if (checkDownloadedFile()) {
            deleteOldFile();
        }
    }

    protected abstract boolean checkDownloadedFile();

    protected abstract void deleteOldFile();
}