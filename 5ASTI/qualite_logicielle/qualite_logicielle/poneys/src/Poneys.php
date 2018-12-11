<?php
/**
 * Gestion de poneys
 */
class Poneys
{
    private $_count = 8;

    /**
     * Retourne le nombre de poneys
     *
     * @return void
     */
    public function getCount(): int
    {
        return $this->_count;
    }

    /**
     * Inialise le nombre de poneys.
     */
    public function setCount(int $number): void
    {
        if($number < 0)            throw new UnexpectedValueException("Nombre negatif");
        if($number > TAILLE_CHAMP) throw new LengthException("Limite de poneys max = ".TAILLE_CHAMP);
        $this->_count = $number;
    }

    /**
     * Retire un poney du champ
     *
     * @param int $number Nombre de poneys à retirer
     *
     * @return void
     */
    public function removePoneyFromField(int $number): void
    {
        if($this->_count < $number) throw new LengthException("Nombre superieur au nombre de poney");
        if($number<0)               throw new UnexpectedValueException("Nombre negatif");
        $this->_count -= $number;
    }

    /**
     * Retourne les noms des poneys
     *
     * @return array
     */
    public function getNames(): array
    {

    }

    /**
     * Ajoute un poney dans le champ
     * 
     */
    public function addPoneysIntoField(int $number): void 
    {
        if($this->_count + $number > TAILLE_CHAMP) throw new LengthException("Trop de poneys a ajouter (".TAILLE_CHAMP."max)"); 
        if($number<0) throw new UnexpectedValueException("Nombre negatif");
        $this->_count += $number;
    }

    /**
     * Retourne TRUE s'il y a de la place pour des poneys supplémentaires (TAILLE_CHAMP max)
     * 
     */
    public function hasRoom(): bool
    {
        return $this->_count < TAILLE_CHAMP;
    }
}
?>
