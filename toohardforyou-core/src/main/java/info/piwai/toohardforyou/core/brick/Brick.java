/**
 * Copyright (C) 2011 Pierre-Yves Ricau (py.ricau at gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package info.piwai.toohardforyou.core.brick;

import info.piwai.toohardforyou.core.Constants;
import info.piwai.toohardforyou.core.EntityEngine;
import info.piwai.toohardforyou.core.TooHardForYouEngine;
import info.piwai.toohardforyou.core.entities.BrokenBrickEntity;
import info.piwai.toohardforyou.core.entities.Entity;
import info.piwai.toohardforyou.core.entities.SolidBrickEntity;
import info.piwai.toohardforyou.core.entities.SolidBrickEntity.BrokenListener;

public class Brick implements BrokenListener {

    protected Entity entity;
    protected final EntityEngine entityEngine;
    protected final TooHardForYouEngine engine;
    protected int wallX;
    protected int wallY;

    public Brick(TooHardForYouEngine engine, EntityEngine entityEngine, BrickType initialBrickType, int wallX, int wallY) {
        this.engine = engine;
        this.entityEngine = entityEngine;
        
        entity = new SolidBrickEntity(entityEngine, initialBrickType, this);
        entityEngine.add(entity);
        
        setPos(wallX, wallY);
    }
    
    private float convertX(int wallX) {
        return Constants.BRICK_WIDTH / 2 + wallX * Constants.BRICK_WIDTH;
    }

    private float convertY(int wallY) {
        return Constants.BRICK_HEIGHT / 2 + wallY * Constants.BRICK_HEIGHT;
    }
    
    public void setPos(int wallX, int wallY) {
        this.wallX = wallX;
        this.wallY = wallY;
        entity.setPos(convertX(wallX), convertY(wallY));
    }

    public void transformPos(int deltaX, int deltaY) {
        setPos(wallX + deltaX, wallY + deltaY);
    }

    public void destroy() {
        entityEngine.remove(entity);
    }

    @Override
    public void hit() {
        entityEngine.remove(entity);
        
        entity = new BrokenBrickEntity(entityEngine);
        entityEngine.add(entity);
        
        setPos(wallX, wallY);

        engine.brickBroken();
    }
    
}