package services;

import beans.Membre;

public interface Facade {
    public Membre logMembre(String login, String motdepasse);
}
