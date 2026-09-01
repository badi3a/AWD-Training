<?php
declare(strict_types=1);
namespace App\Entity;
use App\Repository\UserRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\UserInterface;
#[ORM\Entity(repositoryClass: UserRepository::class), ORM\Table(name:'app_user'), UniqueEntity(fields:['email'])]
class User implements UserInterface, PasswordAuthenticatedUserInterface {
 #[ORM\Id, ORM\GeneratedValue, ORM\Column] private ?int $id=null;
 #[ORM\Column(length:180,unique:true)] private string $email='';
 #[ORM\Column] private array $roles=[];
 #[ORM\Column] private string $password='';
 #[ORM\Column(length:80)] private string $firstName='';
 #[ORM\Column(length:80)] private string $lastName='';
 public function getId():?int{return $this->id;} public function getEmail():string{return $this->email;} public function setEmail(string $v):self{$this->email=strtolower($v);return $this;}
 public function getUserIdentifier():string{return $this->email;} public function getRoles():array{$r=$this->roles;$r[]='ROLE_USER';return array_values(array_unique($r));} public function setRoles(array $v):self{$this->roles=$v;return $this;}
 public function getPassword():string{return $this->password;} public function setPassword(string $v):self{$this->password=$v;return $this;} public function eraseCredentials():void{}
 public function getFirstName():string{return $this->firstName;} public function setFirstName(string $v):self{$this->firstName=$v;return $this;} public function getLastName():string{return $this->lastName;} public function setLastName(string $v):self{$this->lastName=$v;return $this;} public function getFullName():string{return trim($this->firstName.' '.$this->lastName);} }
