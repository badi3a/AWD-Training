<?php
declare(strict_types=1); namespace App\Entity; use App\Repository\SkillRepository; use Doctrine\ORM\Mapping as ORM;
#[ORM\Entity(repositoryClass:SkillRepository::class)] class Skill { #[ORM\Id,ORM\GeneratedValue,ORM\Column] private ?int $id=null; #[ORM\Column(length:100,unique:true)] private string $name=''; public function getId():?int{return $this->id;} public function getName():string{return $this->name;} public function setName(string $v):self{$this->name=$v;return $this;} public function __toString():string{return $this->name;} }
