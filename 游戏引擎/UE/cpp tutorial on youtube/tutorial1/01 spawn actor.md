# spawn actor

```C++
	UFUNCTION(BlueprintCallable, Category="Abilities")
	void SpawnTheActor();

	UPROPERTY(EditAnywhere)
	TSubclassOf<AActor> actorBPToSpawn;
```

​	set the actorBPToSpawn In BluePrint;

```C++
void AMyCharacter::SpawnTheActor()
{
	FActorSpawnParameters spawnParams;
	// 解决碰撞生成点的方法： 一定会生成，如果可以启动适应
	spawnParams.SpawnCollisionHandlingOverride = ESpawnActorCollisionHandlingMethod::AdjustIfPossibleButAlwaysSpawn;

	GetWorld()->SpawnActor<AActor>(actorBPToSpawn, GetActorTransform(), spawnParams);
}
```

