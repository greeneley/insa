<?php
use PHPUnit\Framework\TestCase;

require_once 'src/Poneys.php';

/**
 * Classe de test de gestion de poneys
 */
class PoneysExceptionTest extends TestCase
{
    public function testRemovePoneysFromFieldArgumentNegatif()
    {
        // Setup
        $this->expectException(LengthException::class);
        $Poneys = new Poneys();

        // Action
        $Poneys->removePoneyFromField(9);
    }

    public function testRemovePoneysFromFieldCountNegatif()
    {
        // Setup
        $this->expectException(UnexpectedValueException::class);
        $Poneys = new Poneys();

        // Action
        $Poneys->removePoneyFromField(-1);
    }

    public function testAddPoneysIntoFieldCountMax()
    {
        // Setup
        $this->expectException(LengthException::class);
        $Poneys = new Poneys();

        // Action
        $Poneys->addPoneysIntoField(8);
    }

    public function testAddPoneysIntoFieldArgumentNegatif()
    {
        // Setup
        $this->expectException(UnexpectedValueException::class);
        $Poneys = new Poneys();

        // Action
        $Poneys->removePoneyFromField(-1);
    }

    public function testSetCountMax()
    {
        // Setup
        $this->expectException(LengthException::class);
        $Poneys = new Poneys();

        // Action
        $Poneys->setCount(17);
    }
    public function testSetCountNegatif()
    {
        // Setup
        $this->expectException(UnexpectedValueException::class);
        $Poneys = new Poneys();

        // Action
        $Poneys->setCount(-1);
    }
}