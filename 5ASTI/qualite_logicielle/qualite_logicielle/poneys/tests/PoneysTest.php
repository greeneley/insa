<?php
use PHPUnit\Framework\TestCase;

require_once 'src/Poneys.php';

/**
 * Classe de test de gestion de poneys
 */
class PoneysTest extends TestCase
{
    protected $poneys;

    /**
     * Initialise l'objet 'poneys' avant chaque appel de methode.
     */
    protected function setUp(): void
    {
        $this->poneys = new Poneys();
        $this->poneys->setCount(NOMBRE_PONEYS);
    }

    /**
     * Detruit l'objet 'poneys' a la sortie de chaque methode.
     */
    protected function tearDown()
    {
        $this->poneys = null;
    }

    public function testSetCount()
    {
        $this->poneys->setCount(5);
        $this->assertEquals(5, $this->poneys->getCount());
    }

    /**
     * Undocumented function
     * 
     * @dataProvider removeProvider
     * @return void
     */
    public function testRemovePoneyFromField($a, $expected)
    {
        // Action
        $this->poneys->removePoneyFromField($a);

        // Assert
        $this->assertEquals($expected, $this->poneys->getCount());
    }

    public function removeProvider()
    {
        return [
          [1, 7],
          [2, 6]  
        ];
    }

    /**
     * Undocumented function
     * 
     * @return void
     */
    public function testAddPoneysIntoField()
    {
        // Action
        $this->poneys->addPoneysIntoField(3);

        // Assert
        $this->assertEquals(11, $this->poneys->getCount());
    }

    public function testGetNames()
    {
        // Setup mock
        $poneyMock = $this->getMockBuilder(Poneys::class)
                          ->setMethods(['getNames'])
                          ->getMock();
        
        // Setup method
        $poneyMock->expects($this->once())
                  ->method('getNames')
                  ->will($this->returnValue(["foo", "bar"]));
    
        // Assert
        $this->assertEquals($poneyMock->getNames(), ["foo", "bar"]);
    }

    /**
     * @dataProvider hasRoom
     */
    public function testHasRoom($a, $expected)
    {
        // Action
        $this->poneys->addPoneysIntoField($a);

        // Assert
        $this->assertEquals($expected, $this->poneys->hasRoom());
    }

    public function hasRoom()
    {
        return [
          [0, TRUE],
          [7, FALSE]  
        ];
    }
}
?>
