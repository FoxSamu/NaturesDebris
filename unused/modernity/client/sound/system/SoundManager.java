/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 24 - 2020
 * Author: rgsw
 */

package modernity.client.sound.system;


// TODO Re-evaluate
//@OnlyIn( Dist.CLIENT )
//public class SoundManager extends SoundEngine {
//    private static final Marker LOG_MARKER = MarkerManager.getMarker( "SOUNDS" );
//    private static final Logger LOGGER = LogManager.getLogger();
//
//    private static final Set<ResourceLocation> UNABLE_TO_PLAY = Sets.newHashSet();
//
//    public final SoundHandler handler;
//    private final GameSettings settings;
//
//    private boolean loaded;
//
//    private final SoundSystem sndSystem = new SoundSystem();
//    private final SourceManager srcManager = new SourceManager();
//    private final Listener listener = sndSystem.getListener();
//    private final AudioStreamManager streamManager;
//    private final SoundEngineExecutor executor = new SoundEngineExecutor();
//    private final ChannelManager channelManager = new ChannelManager( srcManager, executor );
//
//    private int ticks;
//
//    private final Map<ISound, ChannelManager.Channel> playingSoundsChannel = Maps.newHashMap();
//    private final Multimap<SoundCategory, ISound> soundByCategory = HashMultimap.create();
//    private final List<ITickableSound> tickableSounds = Lists.newArrayList();
//    private final Map<ISound, Integer> delayedSounds = Maps.newHashMap();
//    private final Map<ISound, Integer> playingSoundsStopTime = Maps.newHashMap();
//    private final List<ISoundEventListener> listeners = Lists.newArrayList();
//    private final List<Sound> soundsToPreload = Lists.newArrayList();
//
//    public SoundManager( SoundHandler handler, GameSettings settings, IResourceManager resManager ) {
//        super( handler, settings, resManager );
//        this.handler = handler;
//        this.settings = settings;
//
//        streamManager = new AudioStreamManager( resManager );
//
//        MinecraftForge.EVENT_BUS.post( new SoundSetupEvent( this ) );
//    }
//
//    @Override
//    public void reload() {
//        UNABLE_TO_PLAY.clear();
//
//        for( SoundEvent ev : Registry.SOUND_EVENT ) {
//            ResourceLocation name = ev.getName();
//            if( handler.getAccessor( name ) == null ) {
//                LOGGER.warn( "Missing sound for event: {}", Registry.SOUND_EVENT.getKey( ev ) );
//                UNABLE_TO_PLAY.add( name );
//            }
//        }
//
//        if( Minecraft.getInstance().world != null ) {
//            Dimension dimen = Minecraft.getInstance().world.dimension;
//            if( dimen instanceof ISoundEffectDimension ) {
//                ( (ISoundEffectDimension) dimen ).cleanupSoundEffects();
//            }
//        }
//
//        unload();
//        load();
//        MinecraftForge.EVENT_BUS.post( new SoundLoadEvent( this ) );
//    }
//
//    /**
//     * Tries to add the paulscode library and the relevant codecs. If it fails, the master volume  will be set to zero.
//     */
//    private synchronized void load() {
//        if( ! loaded ) {
//            try {
//                srcManager.setup();
//
//                listener.init();
//                listener.setGain( settings.getSoundLevel( SoundCategory.MASTER ) );
//
//                streamManager.func_217908_a( soundsToPreload )
//                             .thenRun( soundsToPreload::clear );
//
//                loaded = true;
//                LOGGER.info( LOG_MARKER, "Sound engine started" );
//            } catch( RuntimeException runtimeexception ) {
//                LOGGER.error( LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music", runtimeexception );
//            }
//
//        }
//    }
//
//    private float getVolume( SoundCategory category ) {
//        return category != null && category != SoundCategory.MASTER ? this.settings.getSoundLevel( category ) : 1.0F;
//    }
//
//    @Override
//    public void setVolume( SoundCategory category, float volume ) {
//        if( loaded ) {
//            if( category == SoundCategory.MASTER ) {
//                listener.setGain( volume );
//            } else {
//                playingSoundsChannel.forEach( ( snd, channel ) -> {
//                    float gain = getClampedVolume( snd );
//                    channel.runOnSoundExecutor( src -> {
//                        if( gain <= 0 ) {
//                            src.stop();
//                        } else {
//                            src.setVolume( gain );
//                        }
//
//                    } );
//                } );
//            }
//        }
//    }
//
//    @Override
//    public void unload() {
//        if( loaded ) {
//            stopAllSounds();
//            streamManager.func_217912_a();
//            srcManager.close();
//            loaded = false;
//        }
//
//    }
//
//    @Override
//    public void stop( ISound sound ) {
//        if( loaded ) {
//            ChannelManager.Channel channel = playingSoundsChannel.get( sound );
//            if( channel != null ) {
//                channel.runOnSoundExecutor( EFXSoundSource::stop );
//            }
//        }
//
//    }
//
//    @Override
//    public void stopAllSounds() {
//        if( loaded ) {
//            executor.restart();
//            playingSoundsChannel.values().forEach( channel -> channel.runOnSoundExecutor( EFXSoundSource::stop ) );
//
//            playingSoundsChannel.clear();
//            channelManager.releaseAll();
//            delayedSounds.clear();
//            tickableSounds.clear();
//            soundByCategory.clear();
//            playingSoundsStopTime.clear();
//        }
//
//    }
//
//    @Override
//    public void addListener( ISoundEventListener listener ) {
//        listeners.add( listener );
//    }
//
//    @Override
//    public void removeListener( ISoundEventListener listener ) {
//        listeners.remove( listener );
//    }
//
//    @Override
//    public void tick( boolean sounds ) {
//        if( ! sounds ) {
//            tickSounds();
//        }
//
//        channelManager.tick();
//    }
//
//    private void tickSounds() {
//        ++ ticks;
//
//        for( ITickableSound snd : this.tickableSounds ) {
//            snd.tick();
//            if( snd.isDonePlaying() ) {
//                stop( snd );
//            } else {
//                float gain = getClampedVolume( snd );
//                float pitch = getClampedPitch( snd );
//                Vec3d pos = new Vec3d( snd.getX(), snd.getY(), snd.getZ() );
//                ChannelManager.Channel channel = playingSoundsChannel.get( snd );
//                if( channel != null ) {
//                    channel.runOnSoundExecutor( src -> {
//                        src.setVolume( gain );
//                        src.setPitch( pitch );
//                        src.setPosition( pos );
//                    } );
//                }
//            }
//        }
//
//        Iterator<Entry<ISound, ChannelManager.Channel>> sndIterator = playingSoundsChannel.entrySet().iterator();
//
//        while( sndIterator.hasNext() ) {
//            Entry<ISound, ChannelManager.Channel> pair = sndIterator.next();
//
//            ChannelManager.Channel channel = pair.getValue();
//            ISound sound = pair.getKey();
//
//            float level = settings.getSoundLevel( sound.getCategory() );
//            if( level <= 0 ) {
//                channel.runOnSoundExecutor( EFXSoundSource::stop );
//                sndIterator.remove();
//            } else if( channel.isReleased() ) {
//                int stopTime = playingSoundsStopTime.get( sound );
//                if( stopTime <= ticks ) {
//                    int repeatDelay = sound.getRepeatDelay();
//                    if( sound.canRepeat() && repeatDelay > 0 ) {
//                        this.delayedSounds.put( sound, ticks + repeatDelay );
//                    }
//
//                    sndIterator.remove();
//                    LOGGER.debug( LOG_MARKER, "Removed channel {} because it's not playing anymore", channel );
//                    playingSoundsStopTime.remove( sound );
//
//                    try {
//                        soundByCategory.remove( sound.getCategory(), sound );
//                    } catch( RuntimeException ignored ) {}
//
//                    if( sound instanceof ITickableSound ) {
//                        tickableSounds.remove( sound );
//                    }
//                }
//            }
//        }
//
//        Iterator<Entry<ISound, Integer>> delayedSndIterator = delayedSounds.entrySet().iterator();
//
//        while( delayedSndIterator.hasNext() ) {
//            Entry<ISound, Integer> pair = delayedSndIterator.next();
//            if( ticks >= pair.getValue() ) {
//                ISound snd = pair.getKey();
//                if( snd instanceof ITickableSound ) {
//                    ( (ITickableSound) snd ).tick();
//                }
//
//                play( snd );
//                delayedSndIterator.remove();
//            }
//        }
//
//    }
//
//    @Override
//    public boolean isPlaying( ISound snd ) {
//        if( ! loaded ) {
//            return false;
//        } else {
//            return playingSoundsStopTime.containsKey( snd )
//                       && playingSoundsStopTime.get( snd ) <= ticks
//                       || playingSoundsChannel.containsKey( snd );
//        }
//    }
//
//    @Override
//    public void play( ISound snd ) {
//        if( loaded ) {
//            snd = ForgeHooksClient.playSound( this, snd );
//            if( snd == null ) return;
//            SoundEventAccessor accessor = snd.createAccessor( this.handler );
//            ResourceLocation loc = snd.getSoundLocation();
//
//            if( accessor == null ) {
//                if( UNABLE_TO_PLAY.add( loc ) ) {
//                    LOGGER.warn( LOG_MARKER, "Unable to play unknown soundEvent: {}", loc );
//                }
//            } else {
//                if( ! listeners.isEmpty() ) {
//                    for( ISoundEventListener listener : this.listeners ) {
//                        listener.onPlaySound( snd, accessor );
//                    }
//                }
//
//                if( this.listener.getGain() <= 0 ) {
//                    LOGGER.debug( LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", loc );
//                } else {
//                    Sound sound = snd.getSound();
//                    if( sound == SoundHandler.MISSING_SOUND ) {
//                        if( UNABLE_TO_PLAY.add( loc ) ) {
//                            LOGGER.warn( LOG_MARKER, "Unable to play empty soundEvent: {}", loc );
//                        }
//                    } else {
//                        float vol = snd.getVolume();
//                        float attDistance = Math.max( vol, 1 ) * (float) sound.getAttenuationDistance();
//
//                        SoundCategory cgr = snd.getCategory();
//
//                        float gain = getClampedVolume( snd );
//                        float pitch = getClampedPitch( snd );
//
//                        ISound.AttenuationType attType = snd.getAttenuationType();
//                        boolean global = snd.isGlobal();
//
//                        if( gain == 0 && ! snd.canBeSilent() ) {
//                            LOGGER.debug( LOG_MARKER, "Skipped playing sound {}, volume was zero.", sound.getSoundLocation() );
//                        } else {
//                            boolean repeat = snd.canRepeat() && snd.getRepeatDelay() == 0;
//                            Vec3d pos = new Vec3d( snd.getX(), snd.getY(), snd.getZ() );
//
//                            ChannelManager.Channel channel = channelManager.createChannel( sound.isStreaming() ? SoundSystem.Mode.STREAMING : SoundSystem.Mode.STATIC );
//                            LOGGER.debug( LOG_MARKER, "Playing sound {} for event {}", sound.getSoundLocation(), loc );
//
//                            playingSoundsStopTime.put( snd, ticks + 20 );
//                            playingSoundsChannel.put( snd, channel );
//                            soundByCategory.put( cgr, snd );
//
//                            SoundEffectEvent event = new SoundEffectEvent( snd );
//                            MinecraftForge.EVENT_BUS.post( event );
//                            ISoundSourceEffect effect = event.getEffect();
//
//                            channel.runOnSoundExecutor( src -> {
//                                src.setPitch( pitch );
//                                src.setVolume( gain );
//                                src.setEffect( effect );
//                                if( attType == ISound.AttenuationType.LINEAR ) {
//                                    src.setMaxDistance( attDistance );
//                                } else {
//                                    src.setNoDistanceModel();
//                                }
//
//                                src.setLooping( repeat );
//                                src.setPosition( pos );
//                                src.setRelative( global );
//                            } );
//
//                            ISound finalSnd = snd;
//                            if( ! sound.isStreaming() ) {
//                                streamManager.func_217909_a( sound.getSoundAsOggLocation() )
//                                             .thenAccept( buffer -> channel.runOnSoundExecutor( src -> {
//                                                 src.setBuffer( buffer );
//                                                 src.play();
//                                                 MinecraftForge.EVENT_BUS.post( new PlaySoundSourceEvent( this, finalSnd, src.getWrapped() ) );
//                                             } ) );
//                            } else {
//                                streamManager.func_217917_b( sound.getSoundAsOggLocation() )
//                                             .thenAccept( stream -> channel.runOnSoundExecutor( src -> {
//                                                 src.setStream( stream );
//                                                 src.play();
//                                                 MinecraftForge.EVENT_BUS.post( new PlayStreamingSourceEvent( this, finalSnd, src.getWrapped() ) );
//                                             } ) );
//                            }
//
//                            if( snd instanceof ITickableSound ) {
//                                tickableSounds.add( (ITickableSound) snd );
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void enqueuePreload( Sound snd ) {
//        soundsToPreload.add( snd );
//    }
//
//    private float getClampedPitch( ISound snd ) {
//        return MathHelper.clamp( snd.getPitch(), 0.5F, 2 );
//    }
//
//    private float getClampedVolume( ISound snd ) {
//        return MathHelper.clamp( snd.getVolume() * getVolume( snd.getCategory() ), 0, 1 );
//    }
//
//    @Override
//    public void pause() {
//        if( loaded ) {
//            channelManager.runOnSoundExecutor( stream -> {
//                stream.forEach( EFXSoundSource::pause );
//            } );
//        }
//
//    }
//
//    @Override
//    public void resume() {
//        if( loaded ) {
//            channelManager.runOnSoundExecutor( stream -> stream.forEach( EFXSoundSource::resume ) );
//        }
//
//    }
//
//    @Override
//    public void playDelayed( ISound sound, int delay ) {
//        delayedSounds.put( sound, ticks + delay );
//    }
//
//    @Override
//    public void updateListener( ActiveRenderInfo info ) {
//        if( this.loaded && info.isValid() ) {
//            Vec3d pos = info.getProjectedView();
//            Vec3d dir = info.getLookDirection();
//            Vec3d up = info.getUpDirection();
//            executor.execute( () -> {
//                listener.setPosition( pos );
//                listener.setOrientation( dir, up );
//            } );
//        }
//    }
//
//    @Override
//    public void stop( @Nullable ResourceLocation soundName, @Nullable SoundCategory category ) {
//        if( category != null ) {
//            for( ISound snd : soundByCategory.get( category ) ) {
//                if( soundName == null || snd.getSoundLocation().equals( soundName ) ) {
//                    stop( snd );
//                }
//            }
//        } else if( soundName == null ) {
//            stopAllSounds();
//        } else {
//            for( ISound snd : playingSoundsChannel.keySet() ) {
//                if( snd.getSoundLocation().equals( soundName ) ) {
//                    stop( snd );
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public String getDebugString() {
//        return srcManager.getDebugString();
//    }
//}