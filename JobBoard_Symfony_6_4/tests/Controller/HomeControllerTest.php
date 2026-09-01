<?php
declare(strict_types=1); namespace App\Tests\Controller; use Symfony\Bundle\FrameworkBundle\Test\WebTestCase;
final class HomeControllerTest extends WebTestCase { public function testHomePageLoads():void{$client=static::createClient();$client->request('GET','/');self::assertResponseIsSuccessful();self::assertSelectorTextContains('.brand','JobBoard');} }
