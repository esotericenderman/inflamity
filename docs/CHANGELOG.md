# Changelog

## Format

```markdown
### Version

#### Additions

#### Changes

#### Removals

#### Fixes
```

## Log

### 0.4.0

#### Changes

- Allowed fire to spread from a passenger to its vehicle and vice versa.

### 0.3.1

#### Fixes

- Fixed some "extremelly flammable" items still being able to appear as if on fire.
- Fixed creepers who are right-clicked with a flint and steel being aflame as well as ignited at the same time.

### 0.3.0

#### Additions

- If a slime splits while burning, the resulting smaller slimes will also be on fire.
- Made creepers ignite as soon as they are on fire.
- Projectiles will now carry the fire of their shooter.
- Lit projectiles will now light blocks that they are shot at.
- Lit projectiles will now light candles.

#### Changes

- Igniting entities will now anger them.
- Iron golems are now immune to fire.
- Some entities, such as snowballs, will instantly be removed when entering fire.
- Eggs, ender pearls, and splash potions will no longer have visual fire.
- Snow golems will no longer burn when outside fire.
- Fire does not spread anywhere, unless it makes sense for it to be there (some sort of flammable block nearby).

### 0.2.1

#### Fixes

- Flint and steels will now take durability damage when using them to ignite mobs.

### 0.2.0

#### Changes

- Fire protection now protects even better against fire damage. Full fire protection makes you completely immune to fire.
- Fire protection will sometimes prevent durability loss from fire, with full fire protection preventing all durability loss.

### 0.1.0

#### Additions

- Entities that are on fire will spread fire to blocks around them.
- Hitting a mob that is on fire will spread the fire to you, and vice versa.
- Right-clicking entities with a flint and steel now sets them on fire.

#### Changes

- Attempting to put out a fire with your hand will instead set you on fire.

#### Removals

- Entities on fire do not extinguish naturally.
